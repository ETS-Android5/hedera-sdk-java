import com.google.errorprone.annotations.Var;
import com.hedera.hashgraph.sdk.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ScheduleCreateIntegrationTest {
    @Test
    @DisplayName("Can create schedule")
    void canCreateSchedule() {
        assertDoesNotThrow(() -> {
            var client = IntegrationTestClientManager.getClient();
            var operatorKey = Objects.requireNonNull(client.getOperatorPublicKey());
            var operatorId = Objects.requireNonNull(client.getOperatorAccountId());

            var key = PrivateKey.generate();

            var transaction = new AccountCreateTransaction()
                .setKey(key)
                .setInitialBalance(new Hbar(10))
                .setNodeAccountIds(Collections.singletonList(new AccountId(3)))
                .freezeWith(client);

            var response = new ScheduleCreateTransaction()
                .setTransaction(transaction)
                .setAdminKey(operatorKey)
                .setPayerAccountId(operatorId)
                .execute(client);

            var scheduleId = Objects.requireNonNull(response.getReceipt(client).scheduleId);

            var info = new ScheduleInfoQuery()
                .setScheduleId(scheduleId)
                .setNodeAccountIds(Collections.singletonList(response.nodeId))
                .execute(client);

            new ScheduleDeleteTransaction()
                .setScheduleId(scheduleId)
                .setNodeAccountIds(Collections.singletonList(response.nodeId))
                .execute(client)
                .getReceipt(client);

            client.close();
        });
    }
}
