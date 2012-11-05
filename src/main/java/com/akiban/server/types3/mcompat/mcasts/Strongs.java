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

package com.akiban.server.types3.mcompat.mcasts;

import com.akiban.server.types3.TStrongCasts;
import com.akiban.server.types3.mcompat.mtypes.MApproximateNumber;
import com.akiban.server.types3.mcompat.mtypes.MBinary;
import com.akiban.server.types3.mcompat.mtypes.MDatetimes;
import com.akiban.server.types3.mcompat.mtypes.MNumeric;
import com.akiban.server.types3.mcompat.mtypes.MString;

public final class Strongs {

    public static final TStrongCasts fromStrings = TStrongCasts.
            from(
                MString.VARCHAR, MString.CHAR, MString.TINYTEXT, MString.TEXT, MString.MEDIUMTEXT, MString.LONGTEXT,
                MBinary.VARBINARY, MBinary.BINARY, MBinary.TINYBLOB, MBinary.BLOB, MBinary.MEDIUMBLOB, MBinary.LONGBLOB)
            .to(MDatetimes.DATE,
                MDatetimes.DATETIME,
                MDatetimes.TIME,
                MDatetimes.TIMESTAMP,
                MDatetimes.YEAR,
                MApproximateNumber.DOUBLE);

    public static final TStrongCasts textsToVarchar = TStrongCasts
            .from(MString.CHAR, MString.TINYTEXT, MString.TEXT, MString.MEDIUMTEXT, MString.LONGTEXT)
            .to(MString.VARCHAR);

    public static final TStrongCasts blobsToBinary = TStrongCasts
            .from(MBinary.BINARY, MBinary.TINYBLOB, MBinary.BLOB, MBinary.MEDIUMBLOB, MBinary.LONGBLOB)
            .to(MBinary.VARBINARY);

    public static final TStrongCasts charsToBinaries = TStrongCasts
            .from(MString.VARCHAR, MString.CHAR, MString.TINYTEXT, MString.TEXT, MString.MEDIUMTEXT, MString.LONGTEXT)
            .to(MBinary.VARBINARY, MBinary.BINARY, MBinary.TINYBLOB, MBinary.BLOB, MBinary.MEDIUMBLOB, MBinary.LONGBLOB);

    public static final TStrongCasts fromChar = TStrongCasts.from(MString.CHAR).to(
            MString.TINYTEXT,
            MString.TEXT,
            MString.MEDIUMTEXT,
            MString.LONGTEXT
    );

    public static final TStrongCasts fromTinytext = TStrongCasts.from(MString.TINYTEXT).to(
            MString.TEXT,
            MString.MEDIUMTEXT,
            MString.LONGTEXT
    );

    public static final TStrongCasts fromText = TStrongCasts.from(MString.TEXT).to(
            MString.MEDIUMTEXT,
            MString.LONGTEXT
    );

    public static final TStrongCasts fromMediumtext = TStrongCasts.from(MString.MEDIUMTEXT).to(
            MString.LONGTEXT
    );

    public static final TStrongCasts fromBinary = TStrongCasts.from(MBinary.BINARY).to(
            MBinary.TINYBLOB,
            MBinary.BLOB,
            MBinary.MEDIUMBLOB,
            MBinary.LONGBLOB
    );

    public static final TStrongCasts fromTinyblob = TStrongCasts.from(MBinary.TINYBLOB).to(
            MBinary.BLOB,
            MBinary.MEDIUMBLOB,
            MBinary.LONGBLOB
    );

    public static final TStrongCasts fromBlob = TStrongCasts.from(MBinary.BLOB).to(
            MBinary.MEDIUMBLOB,
            MBinary.LONGBLOB
    );

    public static final TStrongCasts fromMediumblob = TStrongCasts.from(MBinary.MEDIUMBLOB).to(
            MBinary.LONGBLOB
    );

    public static final TStrongCasts fromint = TStrongCasts.from(MNumeric.TINYINT).to(
            MNumeric.SMALLINT,
            MNumeric.MEDIUMINT,
            MNumeric.INT,
            MNumeric.BIGINT,
            MNumeric.SMALLINT_UNSIGNED,
            MNumeric.MEDIUMINT_UNSIGNED,
            MNumeric.INT_UNSIGNED,
            MNumeric.BIGINT_UNSIGNED,
            MNumeric.DECIMAL,
            MApproximateNumber.DOUBLE
    );
    public static final TStrongCasts fromSmallint = TStrongCasts.from(MNumeric.SMALLINT).to(
            MNumeric.MEDIUMINT,
            MNumeric.INT,
            MNumeric.BIGINT,
            MNumeric.MEDIUMINT_UNSIGNED,
            MNumeric.INT_UNSIGNED,
            MNumeric.BIGINT_UNSIGNED,
            MNumeric.DECIMAL,
            MApproximateNumber.DOUBLE
    );
    public static final TStrongCasts fromMediumint = TStrongCasts.from(MNumeric.MEDIUMINT).to(
            MNumeric.INT,
            MNumeric.BIGINT,
            MNumeric.INT_UNSIGNED,
            MNumeric.BIGINT_UNSIGNED,
            MNumeric.DECIMAL,
            MApproximateNumber.DOUBLE
    );
    public static final TStrongCasts fromInt = TStrongCasts.from(MNumeric.INT).to(
            MNumeric.BIGINT,
            MNumeric.BIGINT_UNSIGNED,
            MNumeric.DECIMAL,
            MApproximateNumber.DOUBLE
    );
    public static final TStrongCasts fromBigint = TStrongCasts.from(MNumeric.BIGINT).to(
            MNumeric.DECIMAL,
            MApproximateNumber.DOUBLE
    );


    public static final TStrongCasts fromTinyintUnsigned = TStrongCasts.from(MNumeric.TINYINT_UNSIGNED).to(
            MNumeric.SMALLINT_UNSIGNED,
            MNumeric.MEDIUMINT_UNSIGNED,
            MNumeric.INT_UNSIGNED,
            MNumeric.BIGINT_UNSIGNED,
            MNumeric.DECIMAL,
            MNumeric.DECIMAL_UNSIGNED,
            MApproximateNumber.DOUBLE
    );
    public static final TStrongCasts fromSmallintUnsigned = TStrongCasts.from(MNumeric.SMALLINT_UNSIGNED).to(
            MNumeric.MEDIUMINT_UNSIGNED,
            MNumeric.INT_UNSIGNED,
            MNumeric.BIGINT_UNSIGNED,
            MNumeric.DECIMAL,
            MNumeric.DECIMAL_UNSIGNED,
            MApproximateNumber.DOUBLE
    );
    public static final TStrongCasts fromMediumintUnsigned = TStrongCasts.from(MNumeric.MEDIUMINT_UNSIGNED).to(
            MNumeric.INT_UNSIGNED,
            MNumeric.BIGINT_UNSIGNED,
            MNumeric.DECIMAL,
            MNumeric.DECIMAL_UNSIGNED,
            MApproximateNumber.DOUBLE
    );
    public static final TStrongCasts fromIntUnsigned = TStrongCasts.from(MNumeric.INT_UNSIGNED).to(
            MNumeric.BIGINT_UNSIGNED,
            MNumeric.DECIMAL,
            MNumeric.DECIMAL_UNSIGNED,
            MApproximateNumber.DOUBLE
    );
    public static final TStrongCasts fromBigintUnsigned = TStrongCasts.from(MNumeric.BIGINT_UNSIGNED).to(
            MNumeric.DECIMAL,
            MNumeric.DECIMAL_UNSIGNED,
            MApproximateNumber.DOUBLE
    );

    public static final TStrongCasts fromDatetime = TStrongCasts.from(MDatetimes.DATETIME).to(
            MApproximateNumber.DOUBLE
    );

    public static final TStrongCasts fromDate = TStrongCasts.from(MDatetimes.DATE).to(
            MDatetimes.DATETIME,
            MApproximateNumber.DOUBLE
    );

    public static final TStrongCasts fromTime = TStrongCasts.from(MDatetimes.TIME).to(
            MApproximateNumber.DOUBLE
    );

    public static final TStrongCasts fromYear = TStrongCasts.from(MDatetimes.YEAR).to(
            MApproximateNumber.DOUBLE
    );

    public static final TStrongCasts fromTimestamp = TStrongCasts.from(MDatetimes.TIMESTAMP).to(
            MApproximateNumber.DOUBLE
    );

    public static final TStrongCasts fromDecimal = TStrongCasts.from(MNumeric.DECIMAL).to(
            MNumeric.DECIMAL_UNSIGNED,
            MApproximateNumber.DOUBLE
    );

    public static final TStrongCasts fromDecimalUnsigned = TStrongCasts.from(MNumeric.DECIMAL_UNSIGNED).to(
            MApproximateNumber.DOUBLE
    );

    public static final TStrongCasts fromFloat = TStrongCasts.from(MApproximateNumber.FLOAT).to(
            MApproximateNumber.DOUBLE
    );

    public static final TStrongCasts fromFloatUnsigned = TStrongCasts.from(MApproximateNumber.FLOAT_UNSIGNED).to(
            MApproximateNumber.DOUBLE
    );

    public static final TStrongCasts fromDoubleUnsigned = TStrongCasts.from(MApproximateNumber.DOUBLE_UNSIGNED).to(
            MApproximateNumber.DOUBLE
    );
}
