/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class Test {
    private int a;

    public Test(int a) {
        this.a = a;
    }

    public static void main(String[] args) {
        Test a = new Test(5);
        int b = a.a;
        System.out.println(b);
    }
}
