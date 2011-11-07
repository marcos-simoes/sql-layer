/**
 * Copyright (C) 2011 Akiban Technologies Inc.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License, version 3,
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see http://www.gnu.org/licenses.
 */

package com.akiban.server.test.it.qp;

import com.akiban.ais.model.GroupTable;
import com.akiban.qp.persistitadapter.OperatorStore;
import com.akiban.qp.persistitadapter.PersistitAdapter;
import com.akiban.qp.operator.API;
import com.akiban.qp.operator.Cursor;
import com.akiban.qp.operator.Operator;
import com.akiban.qp.row.RowBase;
import com.akiban.qp.rowtype.RowType;
import com.akiban.qp.rowtype.Schema;
import com.akiban.server.api.dml.scan.NewRow;
import com.akiban.server.expression.Expression;
import com.akiban.server.store.PersistitStore;
import com.akiban.server.store.Store;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static com.akiban.server.expression.std.Expressions.field;
import static com.akiban.qp.operator.API.*;

// More Sort_Tree testing, with randomly generated data

public class Sort_Tree_RandomIT extends OperatorITBase
{
    @Before
    public void before()
    {
        // Don't call super.before(). This is a different schema from most operator ITs.
        t = createTable(
            "schema", "t",
            "a int not null",
            "b int not null",
            "c int not null",
            "d int not null",
            "id int not null key");
        schema = new Schema(rowDefCache().ais());
        tRowType = schema.userTableRowType(userTable(t));
        group = groupTable(t);
        List<NewRow> rows = new ArrayList<NewRow>();
        Random random = new Random(123456789);
        long key = 0;
        for (long a = 0; a < A; a++) {
            int nB = random.nextInt(R) + 1;
            for (long b = 0; b < nB; b++) {
                int nC = random.nextInt(R) + 1;
                for (long c = 0; c < nC; c++) {
                    int nD = random.nextInt(R) + 1;
                    for (long d = 0; d < nD; d++) {
                        NewRow row = createNewRow(t, a, b, c, d, key++);
                        rows.add(row);
                    }
                }
            }
        }
        db = new NewRow[rows.size()];
        rows.toArray(db);
        adapter = persistitAdapter(schema);
        use(db);
    }

    @Test
    public void testSort()
    {
        for (int x = 0; x < 16; x++) {
            boolean aAsc = (x & 8) != 0;
            boolean bAsc = (x & 4) != 0;
            boolean cAsc = (x & 2) != 0;
            boolean dAsc = (x & 1) != 0;
            Operator plan =
                sort_Tree(
                    groupScan_Default(group),
                    tRowType,
                    ordering(field(tRowType, 0), aAsc, field(tRowType, 1), bAsc, field(tRowType, 2), cAsc, field(tRowType, 3), dAsc));
            Cursor cursor = cursor(plan, adapter);
            compareRows(expected(aAsc, bAsc, cAsc, dAsc), cursor);
        }
    }

    private RowBase[] expected(final boolean ... asc)
    {
        RowBase[] sorted = new RowBase[db.length];
        Comparator<NewRow> comparator =
            new Comparator<NewRow>()
            {
                @Override
                public int compare(NewRow x, NewRow y)
                {
                    int c = 0;
                    for (int i = 0; c == 0 && i < 4; i++) {
                        c = compare(x, y, asc, i);
                    }
                    return c;
                }

                private int compare(NewRow x, NewRow y, boolean[] asc, int i)
                {
                    return (int) (((Long) x.get(i)) - ((Long) y.get(i))) * (asc[i] ? 1 : -1);
                }
            };
        Arrays.sort(db, comparator);
        int r = 0;
        for (NewRow dbRow : db) {
            Object[] fields = new Object[]{dbRow.get(0), dbRow.get(1), dbRow.get(2), dbRow.get(3), dbRow.get(4)};
            sorted[r++] = new TestRow(tRowType, fields);
        }
        return sorted;
    }

    private Ordering ordering(Object... objects)
    {
        Ordering ordering = API.ordering();
        int i = 0;
        while (i < objects.length) {
            Expression expression = (Expression) objects[i++];
            Boolean ascending = (Boolean) objects[i++];
            ordering.append(expression, ascending);
        }
        return ordering;
    }

    private static final int A = 100; // Number of distinct t.a values
    private static final int R = 3; // Maximum number of t.b values per a, c values per b, d values per c

    private int t;
    private RowType tRowType;
    private GroupTable group;
}
