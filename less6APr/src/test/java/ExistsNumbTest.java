import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ExistsNumbTest {
    @Parameterized.Parameters
    public static Collection<Object[]> params() {
        return Arrays.asList(new Object[][]{
                {false,new int[]{1,  2, 3,  1, 7}},
                {true,new int[]{1, 2,  2, 3,  1, 7, 4, 5}},
                {true,new int[]{1, 2,  2, 3, 4, 1 }}
        });
    }

    private boolean res;
    private int[] inarr;

    private  Main mobj;

    public ExistsNumbTest(boolean res, int[] inarr) {
        this.res=res;
        this.inarr=inarr;
    }

    @Before
    public void init() {
        mobj=new Main();
    }

    @Test
    public void checkNumbTest() {
       Assert.assertEquals(res, mobj.checkNumb(inarr));
    }
}

