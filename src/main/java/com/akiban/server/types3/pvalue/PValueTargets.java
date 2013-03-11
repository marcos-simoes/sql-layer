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

package com.akiban.server.types3.pvalue;

import com.akiban.server.error.AkibanInternalException;
import com.akiban.server.types3.DeepCopiable;
import com.akiban.server.types3.TInstance;

public final class PValueTargets {
    private PValueTargets() {}

    public static void putLong(PValueTarget target, long val)
    {
        switch (pUnderlying(target))
        {
            case INT_8:
                target.putInt8((byte)val);
            case INT_16:
                target.putInt16((short)val);
            case INT_32:
                target.putInt32((int)val);
            case INT_64:
                target.putInt64((long)val);
            default:
                throw new AkibanInternalException("Cannot put LONG into " + target.tInstance());
        }
    }
    
    public static PUnderlying pUnderlying(PValueTarget valueTarget){
        return TInstance.pUnderlying(valueTarget.tInstance());
    }

    public static void copyFrom(PValueSource source, PValueTarget target) {
        if (source.isNull()) {
            target.putNull();
            return;
        }
        else if (source.hasCacheValue()) {
            if (target.supportsCachedObjects()) {
                // The BigDecimalWrapper is mutable
                // a shalloow copy won't work.
                Object obj = source.getObject();
                if (obj instanceof DeepCopiable)
                    target.putObject(((DeepCopiable)obj).deepCopy());
                else
                    target.putObject(source.getObject());
                return;
            }
            else if (!source.canGetRawValue()) {
                throw new IllegalStateException("source has only cached object, but no cacher provided: " + source);
            }
        }
        else if (!source.canGetRawValue()) {
            throw new IllegalStateException("source has no value: " + source);
        }
        switch (TInstance.pUnderlying(source.tInstance())) {
        case BOOL:
            target.putBool(source.getBoolean());
            break;
        case INT_8:
            target.putInt8(source.getInt8());
            break;
        case INT_16:
            target.putInt16(source.getInt16());
            break;
        case UINT_16:
            target.putUInt16(source.getUInt16());
            break;
        case INT_32:
            target.putInt32(source.getInt32());
            break;
        case INT_64:
            target.putInt64(source.getInt64());
            break;
        case FLOAT:
            target.putFloat(source.getFloat());
            break;
        case DOUBLE:
            target.putDouble(source.getDouble());
            break;
        case BYTES:
            target.putBytes(source.getBytes());
            break;
        case STRING:
            target.putString(source.getString(), null);
            break;
        default:
            throw new AssertionError(source.tInstance());
        }
    }


}
