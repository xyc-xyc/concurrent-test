package org.sample.tests;


import org.openjdk.jcstress.annotations.*;
import org.openjdk.jcstress.infra.results.II_Result;

@JCStressTest
@Outcome(id = "1, 2", expect = Expect.ACCEPTABLE, desc = "Default outcome.")
@Outcome(             expect = Expect.ACCEPTABLE_INTERESTING, desc = "Interesting outcome.")
@State
public class DoubleCheckedLockingTest {
    int v = 1;
    static class MyObj {
        int a,b,c,d;
        public MyObj(int v) {
            a = v;
            b = a+v;
            c = b+v;
            d = c+v;
        }
    }

    MyObj o ;

    public MyObj getMyObj() {
        if(o==null) {
            synchronized (DoubleCheckedLockingTest.class) {
                if(o==null) {
                    o = new MyObj(v);
                }
            }
        }
        return this.o;
    }
    @Actor
    public void actor1(II_Result r) {
        MyObj o = this.getMyObj();
        r.r1 = o.a;
        r.r2 = o.b;
    }
    @Actor
    public void actor2(II_Result r) {
        MyObj o = this.getMyObj();
        r.r1 = o.a;
        r.r2 = o.b;
    }

}

