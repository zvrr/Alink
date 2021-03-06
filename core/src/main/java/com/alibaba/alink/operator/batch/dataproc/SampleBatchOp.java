package com.alibaba.alink.operator.batch.dataproc;

import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.functions.SampleWithFraction;
import org.apache.flink.ml.api.misc.param.Params;
import org.apache.flink.types.Row;

import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.params.dataproc.SampleParams;

import java.util.Random;

/**
 * Sample the input data with given ratio with or without replacement.
 */
public final class SampleBatchOp extends BatchOperator <SampleBatchOp>
	implements SampleParams <SampleBatchOp> {

	private static final long serialVersionUID = 3662449779915952817L;

	public SampleBatchOp() {
		this(new Params());
	}

	public SampleBatchOp(Params params) {
		super(params);
	}

	public SampleBatchOp(double ratio) {
		this(ratio, false);
	}

	public SampleBatchOp(double ratio, boolean withReplacement) {
		this(new Params()
			.set(RATIO, ratio)
			.set(WITH_REPLACEMENT, withReplacement)
		);
	}

	@Override
	public SampleBatchOp linkFrom(BatchOperator <?>... inputs) {
		BatchOperator <?> in = checkAndGetFirst(inputs);
		long seed = new Random().nextLong();

		DataSet <Row> result = in.getDataSet()
			.mapPartition(new SampleWithFraction(getWithReplacement(), getRatio(), seed));

		this.setOutput(result, in.getSchema());
		return this;
	}

}
