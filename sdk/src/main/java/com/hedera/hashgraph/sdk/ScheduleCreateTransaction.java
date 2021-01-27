package com.hedera.hashgraph.sdk;

import com.google.protobuf.ByteString;
import com.google.protobuf.InvalidProtocolBufferException;
import com.hedera.hashgraph.sdk.proto.ScheduleCreateTransactionBody;
import com.hedera.hashgraph.sdk.proto.ScheduleServiceGrpc;
import com.hedera.hashgraph.sdk.proto.TransactionBody;
import com.hedera.hashgraph.sdk.proto.TransactionResponse;
import io.grpc.MethodDescriptor;

import java.util.LinkedHashMap;

public final class ScheduleCreateTransaction extends Transaction<ScheduleCreateTransaction> {
    private final ScheduleCreateTransactionBody.Builder builder;

    public ScheduleCreateTransaction() {
        builder = ScheduleCreateTransactionBody.newBuilder();

        setMaxTransactionFee(new Hbar(5));
    }

    ScheduleCreateTransaction(LinkedHashMap<TransactionId, LinkedHashMap<AccountId, com.hedera.hashgraph.sdk.proto.Transaction>> txs) throws InvalidProtocolBufferException {
        super(txs);

        builder = bodyBuilder.getScheduleCreate().toBuilder();
    }

    public AccountId getPayerAccountId() {
        return AccountId.fromProtobuf(builder.getPayerAccountID());
    }

    public ScheduleCreateTransaction setPayerAccountId(AccountId id) {
        requireNotFrozen();
        builder.setPayerAccountID(id.toProtobuf());
        return this;
    }


    public Key getAdminKey() {
        return Key.fromProtobufKey(builder.getAdminKey());
    }

    public ScheduleCreateTransaction setAdminKey(Key key) {
        requireNotFrozen();
        builder.setAdminKey(key.toProtobufKey());
        return this;
    }

    public ByteString getTransactionBody() {
        return builder.getTransactionBody();
    }

    public ScheduleCreateTransaction setTransactionBody(byte[] bytes) {
        requireNotFrozen();
        builder.setTransactionBody(ByteString.copyFrom(bytes));
        return this;
    }

    public ScheduleCreateTransaction setTransactionBody(String text) {
        requireNotFrozen();
        builder.setTransactionBody(ByteString.copyFromUtf8(text));
        return this;
    }

    @Override
    MethodDescriptor<com.hedera.hashgraph.sdk.proto.Transaction, TransactionResponse> getMethodDescriptor() {
        return ScheduleServiceGrpc.getCreateScheduleMethod();
    }

    @Override
    boolean onFreeze(TransactionBody.Builder bodyBuilder) {
        bodyBuilder.setScheduleCreate(builder);
        return true;
    }
}
