package com.ben9583.chess_ai.ai.models;

import com.ben9583.chess_ai.components.Board;
import com.ben9583.chess_ai.components.Player;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.inputs.InputType;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.weights.WeightInit;
import org.jetbrains.annotations.Nullable;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.learning.config.Adam;

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

    public NeuralAgent(Board board, Player player, long seed) {
        super(board, player);

        this.RANDOM_SEED = seed;
    }

    public NeuralAgent(Board board, Player player) { this(board, player, new Random().nextLong()); }

    /**
     * Constructs a neural network configuration for this
     * AI Agent using the random seed supplied on construction.
     */
    private void constructNeuralNetwork() {
        this.conf = new NeuralNetConfiguration.Builder()
                .seed(this.RANDOM_SEED)
                .l2(0.0005)
                .weightInit(WeightInit.XAVIER)
                .updater(new Adam(1e-3))
                .list()
                .layer(new ConvolutionLayer.Builder(3, 3)
                        .stride(1, 1)
                        .nOut(16)
                        .activation(Activation.IDENTITY)
                        .build()
                )
                .layer(new SubsamplingLayer.Builder(PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build()
                )
                .layer(new ConvolutionLayer.Builder(3, 3)
                        .stride(1, 1)
                        .nOut(32)
                        .activation(Activation.IDENTITY)
                        .build()
                )
                .layer(new SubsamplingLayer.Builder(PoolingType.MAX)
                        .kernelSize(2, 2)
                        .stride(2, 2)
                        .build()
                )
                .layer(new DenseLayer.Builder()
                        .activation(Activation.RELU)
                        .nOut(128)
                        .build()
                )
                .layer(new OutputLayer.Builder()
                        .nOut(1)
                        .activation(Activation.SIGMOID)
                        .build()
                )
                .setInputType(InputType.convolutionalFlat(8, 8, 6))
                .build();
    }

    @Override
    public float evaluatePosition() {
        return 0;
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
