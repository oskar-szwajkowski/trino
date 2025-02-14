/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.trino.plugin.hudi;

import com.google.common.collect.ImmutableMap;
import io.trino.plugin.hudi.testing.ResourceHudiTablesInitializer;
import io.trino.testing.AbstractTestQueryFramework;
import io.trino.testing.QueryRunner;
import org.junit.jupiter.api.Test;

import static io.trino.plugin.hudi.HudiQueryRunner.createHudiQueryRunner;

public class TestHudiSystemTables
        extends AbstractTestQueryFramework
{
    @Override
    protected QueryRunner createQueryRunner()
            throws Exception
    {
        return createHudiQueryRunner(ImmutableMap.of(), ImmutableMap.of(), new ResourceHudiTablesInitializer());
    }

    @Test
    public void testTimelineTable()
    {
        assertQuery("SHOW COLUMNS FROM tests.\"hudi_cow_pt_tbl$timeline\"",
                "VALUES ('timestamp', 'varchar', '', '')," +
                        "('action', 'varchar', '', '')," +
                        "('state', 'varchar', '', '')");

        assertQuery("SELECT timestamp, action, state FROM tests.\"hudi_cow_pt_tbl$timeline\"",
                "VALUES ('20220906063435640', 'commit', 'COMPLETED'), ('20220906063456550', 'commit', 'COMPLETED')");

        assertQueryFails("SELECT timestamp, action, state FROM tests.\"non_existing$timeline\"",
                ".*Table 'hudi.tests.non_existing\\$timeline' does not exist");
    }
}
