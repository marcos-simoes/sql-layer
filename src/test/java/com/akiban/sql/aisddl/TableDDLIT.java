/**
 * END USER LICENSE AGREEMENT (“EULA”)
 *
 * READ THIS AGREEMENT CAREFULLY (date: 9/13/2011):
 * http://www.akiban.com/licensing/20110913
 *
 * BY INSTALLING OR USING ALL OR ANY PORTION OF THE SOFTWARE, YOU ARE ACCEPTING
 * ALL OF THE TERMS AND CONDITIONS OF THIS AGREEMENT. YOU AGREE THAT THIS
 * AGREEMENT IS ENFORCEABLE LIKE ANY WRITTEN AGREEMENT SIGNED BY YOU.
 *
 * IF YOU HAVE PAID A LICENSE FEE FOR USE OF THE SOFTWARE AND DO NOT AGREE TO
 * THESE TERMS, YOU MAY RETURN THE SOFTWARE FOR A FULL REFUND PROVIDED YOU (A) DO
 * NOT USE THE SOFTWARE AND (B) RETURN THE SOFTWARE WITHIN THIRTY (30) DAYS OF
 * YOUR INITIAL PURCHASE.
 *
 * IF YOU WISH TO USE THE SOFTWARE AS AN EMPLOYEE, CONTRACTOR, OR AGENT OF A
 * CORPORATION, PARTNERSHIP OR SIMILAR ENTITY, THEN YOU MUST BE AUTHORIZED TO SIGN
 * FOR AND BIND THE ENTITY IN ORDER TO ACCEPT THE TERMS OF THIS AGREEMENT. THE
 * LICENSES GRANTED UNDER THIS AGREEMENT ARE EXPRESSLY CONDITIONED UPON ACCEPTANCE
 * BY SUCH AUTHORIZED PERSONNEL.
 *
 * IF YOU HAVE ENTERED INTO A SEPARATE WRITTEN LICENSE AGREEMENT WITH AKIBAN FOR
 * USE OF THE SOFTWARE, THE TERMS AND CONDITIONS OF SUCH OTHER AGREEMENT SHALL
 * PREVAIL OVER ANY CONFLICTING TERMS OR CONDITIONS IN THIS AGREEMENT.
 */

package com.akiban.sql.aisddl;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.akiban.ais.model.AkibanInformationSchema;
import com.akiban.ais.model.Column;
import com.akiban.ais.model.Types;
import com.akiban.ais.model.UserTable;
import com.akiban.server.api.DDLFunctions;
import com.akiban.sql.pg.PostgresServerITBase;
import org.postgresql.util.PSQLException;


public class TableDDLIT extends PostgresServerITBase {

    private static final String DROP_T1 = "DROP TABLE test.t1";
    private static final String DROP_T2 = "DROP TABLE test.t2";
    
    @Test (expected=PSQLException.class)
    public void testDropFail() throws Exception {
        String sql = "DROP TABLE test.not_here";
        getConnection().createStatement().execute(sql);
    }
    
    @Test
    public void testCreateSimple() throws Exception {
        String sqlCreate = "CREATE TABLE test.T1 (c1 integer not null primary key)";
        getConnection().createStatement().execute(sqlCreate);
        
        AkibanInformationSchema ais = ddlServer().getAIS(session());
        assertNotNull (ais.getUserTable ("test", "t1"));
        
        getConnection().createStatement().execute(DROP_T1);

        ais = ddlServer().getAIS(session());
        assertNull (ais.getUserTable("test", "t1"));
    }
    
    @Test 
    public void testCreateIndexes() throws Exception {
        String sql = "CREATE TABLE test.t1 (c1 integer not null primary key, " + 
            "c2 integer not null, " +
            "constraint c2 unique (c2))";
        getConnection().createStatement().execute(sql);
        AkibanInformationSchema ais = ddlServer().getAIS(session());
        
        UserTable table = ais.getUserTable("test", "t1");
        assertNotNull (table);
        
        assertNotNull (table.getPrimaryKey());
        assertEquals ("PRIMARY", table.getPrimaryKey().getIndex().getIndexName().getName());
        
        assertEquals (2, table.getIndexes().size());
        assertNotNull (table.getIndex("PRIMARY"));
        assertNotNull (table.getIndex("c2"));

        getConnection().createStatement().execute(DROP_T1);
        
    }
    
    @Test
    public void testCreateJoin() throws Exception {
        String sql1 = "CREATE TABLE test.t1 (c1 integer not null primary key)";
        String sql2 = "CREATE TABLE test.t2 (c1 integer not null primary key, " +
            "c2 integer not null, grouping foreign key (c2) references test.t1)";
        
        getConnection().createStatement().execute(sql1);
        getConnection().createStatement().execute(sql2);
        AkibanInformationSchema ais = ddlServer().getAIS(session());
        

        UserTable table = ais.getUserTable("test", "t2");
        assertNotNull (table);
        assertEquals (2, ais.getJoins().size());
        assertNotNull (table.getParentJoin());
        getConnection().createStatement().execute(DROP_T2);
        getConnection().createStatement().execute(DROP_T1);
       
    }
    
    @Test
    public void testCreateAutoIncrement() throws Exception {
        String sql = "CREATE TABLE test.t1 (c1 integer not null primary key generated by default as identity " +
                    "(start with 1000, increment by 1))";
        getConnection().createStatement().execute(sql);
        UserTable table = ddlServer().getAIS(session()).getUserTable("test", "t1");
        
        assertNotNull(table);
        assertEquals(table.getColumn(0).getType(), Types.INT);
        assertEquals(table.getPrimaryKey().getColumns().get(0), table.getColumn(0));
        assertEquals(1000L, table.getColumn(0).getInitialAutoIncrementValue().longValue());
    }

    @Test
    public void testCreateInteger() throws Exception {
        
        String sql1 = "CREATE TABLE test.t1 (col1 INTEGER NOT NULL, col2 INTEGER, col3 smallint NOT NULL, "+ 
                    "col4 smallint, col5 bigint NOT NULL, col6 bigint, " +
                    "col7 INTEGER UNSIGNED, col8 smallint unsigned)";
        
        getConnection().createStatement().execute(sql1);
        UserTable table = ddlServer().getAIS(session()).getUserTable("test", "t1");
        
        assertNotNull (table);
        assertEquals (table.getColumn(0).getType(), Types.INT);
        assertFalse  (table.getColumn(0).getNullable());
        assertEquals (table.getColumn(1).getType(), Types.INT);
        assertTrue   (table.getColumn(1).getNullable());
        assertEquals (table.getColumn(2).getType(), Types.SMALLINT);
        assertFalse  (table.getColumn(2).getNullable());
        assertEquals (table.getColumn(3).getType(), Types.SMALLINT);
        assertTrue   (table.getColumn(3).getNullable());
        assertEquals (table.getColumn(4).getType(), Types.BIGINT);
        assertFalse  (table.getColumn(4).getNullable());
        assertEquals (table.getColumn(5).getType(), Types.BIGINT);
        assertTrue   (table.getColumn(5).getNullable());
        assertEquals (table.getColumn(6).getType(), Types.U_INT);
        assertTrue   (table.getColumn(6).getNullable());
        assertEquals (table.getColumn(7).getType(), Types.U_SMALLINT);
        assertTrue   (table.getColumn(7).getNullable());
        
        
    }
    
    @Test
    public void testCreateChar() throws Exception {
        String sql1 = "CREATE TABLE test.T1 (col10 CHAR(1) NOT NULL, col11 CHAR(1), " + 
            "col12 VARCHAR(1) NOT NULL, col13 VARCHAR(1), " +
            " col14 LONG VARCHAR NOT NULL, col15 LONG VARCHAR) ";
        getConnection().createStatement().execute(sql1);
        UserTable table = ddlServer().getAIS(session()).getUserTable("test", "t1");
        
        assertNotNull (table);
        assertEquals (table.getColumn(0).getType(), Types.CHAR);
        assertEquals (table.getColumn(0).getMaxStorageSize().longValue(), 4L);
        assertEquals (table.getColumn(0).getTypeParameter1().longValue(), 1L);
        assertEquals (table.getColumn(1).getType(), Types.CHAR);
        assertEquals (table.getColumn(1).getMaxStorageSize().longValue(), 4L);
        assertEquals (table.getColumn(2).getType(), Types.VARCHAR);
        assertEquals (table.getColumn(2).getMaxStorageSize().longValue(), 4L);
        assertEquals (table.getColumn(3).getType(), Types.VARCHAR);
        assertEquals (table.getColumn(3).getMaxStorageSize().longValue(), 4L);
        assertEquals (table.getColumn(4).getType(), Types.VARCHAR);
        assertFalse   (table.getColumn(4).getNullable());
        assertEquals (table.getColumn(4).getMaxStorageSize().longValue(), 98103L);
        assertEquals (table.getColumn(5).getType(), Types.VARCHAR);
    }

    @Test
    public void testCreateTime() throws Exception {
        String sql1 = "CREATE TABLE test.t1 (col30 DATE NOT NULL, col31 DATE, "+ 
            "col32 TIME NOT NULL, col33 time, col34 timestamp NOT NULL, " +
        		"col35 timestamp)";
        getConnection().createStatement().execute(sql1);
        UserTable table = ddlServer().getAIS(session()).getUserTable("test", "t1");
        
        assertNotNull (table);
        assertEquals (table.getColumn(0).getType(), Types.DATE);
        assertFalse  (table.getColumn(0).getNullable());
        assertEquals (table.getColumn(1).getType(), Types.DATE);
        assertEquals (table.getColumn(2).getType(), Types.TIME);
        assertEquals (table.getColumn(3).getType(), Types.TIME);
        assertEquals (table.getColumn(4).getType(), Types.DATETIME);
        assertEquals (table.getColumn(5).getType(), Types.DATETIME);
    }
    
    @Test
    public void testCreateLOB() throws Exception {
        String sql1 = "CREATE TABLE test.t1 (col40 CLOB NOT NULL, col41 CLOB, col42 BLOB NOT NULL, col43 BLOB)";
        getConnection().createStatement().execute(sql1);
        UserTable table = ddlServer().getAIS(session()).getUserTable("test", "t1");
        
        assertNotNull (table);
        assertEquals (table.getColumn(0).getType(), Types.LONGTEXT);
        assertEquals (table.getColumn(1).getType(), Types.LONGTEXT);
        assertEquals (table.getColumn(2).getType(), Types.LONGBLOB);
        assertEquals (table.getColumn(3).getType(), Types.LONGBLOB);
    }
    
    @Test
    public void testCreateFloat() throws Exception {
        String sql1 = "CREATE TABLE test.t1 (col20 FLOAT NOT NULL,  col21 FLOAT, "+
            "col22 REAL NOT NULL, col23 REAL, col24 DOUBLE NOT NULL, col25 DOUBLE," +
            "col26 DOUBLE UNSIGNED, col27 REAL UNSIGNED, col28 NUMERIC)";
        getConnection().createStatement().execute(sql1);
        UserTable table = ddlServer().getAIS(session()).getUserTable("test", "t1");
        
        assertNotNull (table);
        assertEquals (table.getColumn(0).getType(), Types.DOUBLE);
        assertEquals (table.getColumn(1).getType(), Types.DOUBLE);
        assertEquals (table.getColumn(2).getType(), Types.FLOAT);
        assertEquals (table.getColumn(3).getType(), Types.FLOAT);
        assertEquals (table.getColumn(4).getType(), Types.DOUBLE);
        assertEquals (table.getColumn(5).getType(), Types.DOUBLE);
        assertEquals (table.getColumn(6).getType(), Types.U_DOUBLE);
        assertEquals (table.getColumn(7).getType(), Types.U_FLOAT);
        assertEquals (table.getColumn(8).getType(), Types.DECIMAL);
    }

    @Test
    public void testCreateDecimal () throws Exception {
        String sql1 = "CREATE TABLE test.t1 (col50 DECIMAL NOT NULL, col51 DECIMAL,"+
        "col52 DECIMAL (1) NOT NULL, "+
        "col54 DECIMAL (10) NOT NULL,  " +
        "col57 DECIMAL (1,1), col58 DECIMAL (10,1) NOT NULL, " +
        "col60 DECIMAL (10,10) NOT NULL, " +
        "col63 DECIMAL (30,10))";
        getConnection().createStatement().execute(sql1);
        UserTable table = ddlServer().getAIS(session()).getUserTable("test", "t1");
        
        assertNotNull (table);
        assertEquals (table.getColumn(0).getType(), Types.DECIMAL);
        assertEquals (table.getColumn(0).getTypeParameter1().longValue(), 5L);
        assertEquals (table.getColumn(0).getTypeParameter2().longValue(), 0L);
        assertEquals (table.getColumn(1).getType(), Types.DECIMAL);
        assertTrue   (table.getColumn(1).getNullable());
        assertEquals (table.getColumn(2).getType(), Types.DECIMAL);
        assertEquals (table.getColumn(2).getTypeParameter1().longValue(), 1L);
        assertEquals (table.getColumn(2).getTypeParameter2().longValue(), 0L);
        assertEquals (table.getColumn(3).getType(), Types.DECIMAL);
        assertEquals (table.getColumn(3).getTypeParameter1().longValue(), 10L);
        assertEquals (table.getColumn(3).getTypeParameter2().longValue(), 0L);

        assertEquals (table.getColumn(4).getType(), Types.DECIMAL);
        assertEquals (table.getColumn(4).getTypeParameter1().longValue(), 1L);
        assertEquals (table.getColumn(4).getTypeParameter2().longValue(), 1L);
        assertEquals (table.getColumn(5).getType(), Types.DECIMAL);
        assertEquals (table.getColumn(5).getTypeParameter1().longValue(), 10L);
        assertEquals (table.getColumn(5).getTypeParameter2().longValue(), 1L);
        assertEquals (table.getColumn(6).getType(), Types.DECIMAL);
        assertEquals (table.getColumn(6).getTypeParameter1().longValue(), 10L);
        assertEquals (table.getColumn(6).getTypeParameter2().longValue(), 10L);
        assertEquals (table.getColumn(7).getType(), Types.DECIMAL);
        assertEquals (table.getColumn(7).getTypeParameter1().longValue(), 30L);
        assertEquals (table.getColumn(7).getTypeParameter2().longValue(), 10L);
    }
    
    @Test
    public void createCustomers() throws Exception {
        String sql1 = "create table test.customers (" +
        "customer_id int not null primary key generated by default as identity," + 
        "customer_name varchar(255) not null,"+
        "primary_payment_code char(1) not null default 'C', " +
        "payment_status char(4) not null default 'ABCD', " + 
        "comment varchar(255))";

        String sql2 = "create table test.addresses (" +
        "customer_id int not null," +
        "state varchar(2) not null," +
        "zip_code varchar(5) not null," +
        "phone varchar (15)," + 
        "primary key (customer_id, zip_code)," +
        "grouping foreign key (customer_id) references customers)";

        getConnection().createStatement().execute(sql1);
        UserTable table = ddlServer().getAIS(session()).getUserTable("test", "customers");

        assertNotNull(table);
        assertEquals(5, table.getColumns().size());
        
        getConnection().createStatement().execute(sql2);
        table = ddlServer().getAIS(session()).getUserTable("test", "addresses");

        assertNotNull(table);
        assertEquals(4, table.getColumns().size());
        
    }

    @Test
    public void createIdentity () throws Exception {
        String sql = "CREATE TABLE test.t7 (c1 integer primary key generated by default as identity)";
        getConnection().createStatement().execute(sql);
        UserTable table = ddlServer().getAIS(session()).getUserTable("test", "t7");
        assertNotNull(table);
        Column column = table.getColumn(0);
        
        assertNotNull(column.getIdentityGenerator());
        assertEquals (1, column.getIdentityGenerator().getStartsWith());
        assertEquals (1, column.getIdentityGenerator().getIncrement());
        assertNotNull(column.getDefaultIdentity());
        assertTrue(column.getDefaultIdentity().booleanValue());
    }
    
    @Test
    public void createAlwaysIdentity() throws Exception {
        String sql = "CREATE TABLE test.t9 (c1 integer primary key generated ALWAYS as identity)";
        getConnection().createStatement().execute(sql);
        UserTable table = ddlServer().getAIS(session()).getUserTable("test", "t9");
        assertNotNull(table);
        Column column = table.getColumn(0);
        
        assertNotNull(column.getIdentityGenerator());
        assertEquals (1, column.getIdentityGenerator().getStartsWith());
        assertEquals (1, column.getIdentityGenerator().getIncrement());
        assertNotNull(column.getDefaultIdentity());
        assertFalse(column.getDefaultIdentity().booleanValue());
    }
    
    @Test
    public void createIdentityValues() throws Exception {
        String sql = "CREATE TABLE test.t10 (c1 integer primary key generated by default as identity (start with 11, increment by -1))";
        getConnection().createStatement().execute(sql);
        UserTable table = ddlServer().getAIS(session()).getUserTable("test", "t10");
        assertNotNull(table);
        Column column = table.getColumn(0);
        
        assertNotNull(column.getIdentityGenerator());
        assertEquals (11, column.getIdentityGenerator().getStartsWith());
        assertEquals (-1, column.getIdentityGenerator().getIncrement());
        assertNotNull(column.getDefaultIdentity());
        assertTrue(column.getDefaultIdentity().booleanValue());
        
    }
    
    @Test 
    public void dropTableWithIdentity() throws Exception {
        
        assertEquals (0, ddlServer().getAIS(session()).getSequences().size());
        String sql = "CREATE TABLE test.t11 (c1 integer primary key generated by default as identity)";
        getConnection().createStatement().execute(sql);
        sql = "CREATE TABLE test.t12 (c1 integer primary key generated by default as identity)";
        getConnection().createStatement().execute(sql);
        
        assertEquals (2, ddlServer().getAIS(session()).getSequences().size());
        
        sql = "DROP TABLE test.t11";
        getConnection().createStatement().execute(sql);
        
        assertEquals (1, ddlServer().getAIS(session()).getSequences().size());

    }
    
    protected DDLFunctions ddlServer() {
        return serviceManager().getDXL().ddlFunctions();
    }
}
