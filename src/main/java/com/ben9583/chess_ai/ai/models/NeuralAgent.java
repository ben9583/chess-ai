package com.ben9583.chess_ai.ai.models;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import org.deeplearning4j.nn.conf.CNN2DFormat;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.gradient.DefaultGradient;
import org.deeplearning4j.nn.gradient.Gradient;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.jetbrains.annotations.Nullable;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

/**
 * AI Agent that uses a neural network to evaluate a position
 * which it uses to determine a move to make.
 */
public class NeuralAgent extends EvalAgent{
    /* Random seed used in the neural network. */
    private final long RANDOM_SEED;

    /* Neural network configuration for this agent. If not initialized, will be null. */
    @Nullable
    private MultiLayerConfiguration conf = null;
    /* Neural network model constructed from conf. If not initialized, will be null. */
    @Nullable
    private MultiLayerNetwork model = null;

    /* Height of the board. */
    public static final int BOARD_HEIGHT = 8;
    /* Width of the board. */
    public static final int BOARD_WIDTH = 8;
    /* Number of distinct pieces. PNBRQKpnbrqk = 12.*/
    /* NOTE: May want to consider piece's player as another dimension. */
    public static final int NUM_DISTINCT_PIECES = 12;

    public NeuralAgent(Board board, Player player, long seed) {
        super(board, player);

        this.RANDOM_SEED = seed;
    }

    public NeuralAgent(Board board, Player player) { this(board, player, new Random().nextLong()); }

    /**
     * Constructs a neural network configuration for this
     * AI Agent using the random seed supplied on construction.
     */
    public void constructNeuralNetwork() {
        this.conf = new NeuralNetConfiguration.Builder()
                .seed(this.RANDOM_SEED)
                .l2(0.0005)
                .weightInit(WeightInit.XAVIER)
                .updater(new Adam(1e-3))
                .list()
                .layer(new Convolution2D.Builder(3, 3)
                        .stride(1, 1)
                        .nOut(16)
                        .activation(Activation.IDENTITY)
                        .name("Conv0")
                        .build()
                )
                .layer(new SubsamplingLayer.Builder(PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .name("Sub0")
                        .build()
                )
                .layer(new Convolution2D.Builder(3, 3)
                        .stride(1, 1)
                        .nOut(32)
                        .activation(Activation.IDENTITY)
                        .name("Conv1")
                        .build()
                )
                .layer(new SubsamplingLayer.Builder(PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .name("Sub1")
                        .build()
                )
                .layer(new DenseLayer.Builder()
                        .activation(Activation.RELU)
                        .nOut(128)
                        .name("Dense0")
                        .build()
                )
                .layer(new OutputLayer.Builder(LossFunctions.LossFunction.MSE)
                        .nOut(1)
                        .activation(Activation.SIGMOID)
                        .name("Output")
                        .build()
                )
                .setInputType(InputType.convolutional(NeuralAgent.BOARD_HEIGHT, NeuralAgent.BOARD_WIDTH, NeuralAgent.NUM_DISTINCT_PIECES, CNN2DFormat.NHWC))
                .build();

        this.model = new MultiLayerNetwork(this.conf);
        model.init();
        this.model.setGradient(new DefaultGradient(Nd4j.zeros(1, 10737)));
    }

    /**
     * Returns the MultiLayerNetwork model for this agent.
     * @return Model for this agent.
     */
    public @Nullable MultiLayerNetwork getModel() {
        return this.model;
    }

    /**
     * Sets the MultiLayerNetwork model for this agent.
     * @param model The new model for this agent.
     */
    public void setModel(@Nullable MultiLayerNetwork model) {
        this.model = model;
    }

    /**
     * Sets the gradient of this agent's model randomly and
     * mutates the parameters using the gradient.
     */
    public void mutate() {
        Gradient grad = new DefaultGradient();
        grad.setGradientFor("0_W", Nd4j.rand(16, 12, 3, 3).sub(0.5).mul(10));
        grad.setGradientFor("2_W", Nd4j.rand(32, 16, 3, 3).sub(0.5).mul(10));
        grad.setGradientFor("4_W", Nd4j.rand(32, 128).sub(0.5).mul(10));
        grad.setGradientFor("5_W", Nd4j.rand(128, 1).sub(0.5).mul(10));
        grad.setGradientFor("0_b", Nd4j.rand(16).sub(0.5).mul(10));
        grad.setGradientFor("2_b", Nd4j.rand(32).sub(0.5).mul(10));
        grad.setGradientFor("4_b", Nd4j.rand(128).sub(0.5).mul(10));
        grad.setGradientFor("5_b", Nd4j.rand(1).sub(0.5).mul(10));

        this.getModel().update(grad);
    }

    /**
     * Loads the parameter data from path into this agent.
     * @param path Path to the .dat file containing the INDArray for the parameters.
     * @return True if the load was successful, false otherwise.
     */
    public boolean loadDataFromFile(String path) {
        try {
            FileInputStream fis = new FileInputStream(path);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Gradient data = (Gradient) new ObjectInputStream(new FileInputStream(path)).readObject();
            this.getModel().setGradient(data);
            fis.close();
            ois.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    /**
     * Saves the parameter data from this agent to path.
     * @param path Path to the .dat file to save the INDArray for the parameters.
     * @return True if the save was successful, false otherwise.
     */
    public boolean saveDataToFile(String path) {
        try {
            FileOutputStream fos = new FileOutputStream(path);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(this.getModel().getGradient());
            fos.close();
            oos.close();
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    @Override
    public float[] evaluatePositions(float[][][][] boards) {
        if(this.model == null) throw new RuntimeException("Model for " + this + " is not yet initialized, but tried to use anyway.");

        INDArray input = Nd4j.createFromArray(boards);
        INDArray output = this.model.output(input);

        float[] out = output.toFloatVector();
        return out;
    }

    @Override
    public String promote() {
        return "Queen";
    }

    @Override
    public boolean shouldResign() {
        return false;
    }
}
