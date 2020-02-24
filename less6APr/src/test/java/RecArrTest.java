import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class RecArrTest {
    @Parameterized.Parameters
    public static Collection<Object[]> params() {
        return Arrays.asList(new Object[][]{
                {new int[]{1,7},new int[]{1, 2, 4, 4, 2, 3, 4, 1, 7}},
                {new int[]{1,5},new int[]{1, 2,  2, 3,  1, 7, 4, 1, 5}},
                {new int[]{},new int[]{1, 2,  2, 3,  1, 7, 4}}
        });
    }

    private int[] inarr;
    private int[] outarr;

    private  Main mobj;

    public RecArrTest(int[] outarr, int[] inarr) {
        this.inarr=inarr;
        this.outarr=outarr;
    }

    @Before
    public void init() {
      mobj=new Main();
    }

    @Test
    public void recArrTest() {
        Assert.assertArrayEquals(outarr,mobj.recArr(inarr));
     }
}
