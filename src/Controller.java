public class Controller {

    public Controller(){

    }
    //定义两个替换盒，都是二位数组
    static int[][][] S1 = {{{0, 1}, {0, 0}, {1, 1}, {1, 0}},
            {{1, 1}, {1, 0}, {0, 1}, {0, 0}},
            {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
            {{1, 1}, {0, 1}, {0, 0}, {1, 0}}};

    static int[][][] S2 = {{{0, 0}, {0, 1}, {1, 0}, {1, 1}},
            {{1, 0}, {1, 1}, {0, 1}, {0, 0}},
            {{1, 1}, {0, 0}, {0, 1}, {1, 0}},
            {{1, 0}, {0, 1}, {0, 0}, {1, 1}}};

    public static void createKey(int[] k, int[] k1, int[] k2) {
        int[] temp = new int[11];//用于存储第一次P10的结果
        temp[1] = k[3]; temp[2] = k[5]; temp[3] = k[2]; temp[4] = k[7];
        temp[5] = k[4]; temp[6] = k[10]; temp[7] = k[1]; temp[8] = k[9];
        temp[9] = k[8]; temp[10] = k[6];

        //左右拆分
        int[] l = new int[6], r = new int[6];
        //将数组 temp 中从索引 1 开始的 5 个元素复制到数组 l 中，从索引 1 开始的位置。
        System.arraycopy(temp, 1, l, 1, 5);
        System.arraycopy(temp, 6, r, 1, 5);

        //实现移位操作
        int x1 = l[1], x2 = r[1];
        for (int i = 2; i <= 5; i++) {
            l[i - 1] = l[i];
            r[i - 1] = r[i];
        }
        l[5] = x1; r[5] = x2;

        //分为左右两边移位结果写入temp
        System.arraycopy(l, 1, temp, 1, 5);
        System.arraycopy(r, 1, temp, 6, 5);

        //进行一个P8的压缩置换后即得到k1
        k1[1] = temp[6]; k1[2] = temp[3]; k1[3] = temp[7];
        k1[4] = temp[4]; k1[5] = temp[8]; k1[6] = temp[5];
        k1[7] = temp[10]; k1[8] = temp[9];

        //再进行一次移位
        x1 = l[1];  x2 = r[1];
        for (int i = 2; i <= 5; i++) {
            l[i - 1] = l[i];
            r[i - 1] = r[i];
        }
        l[5] = x1; r[5] = x2;

        System.arraycopy(l, 1, temp, 1, 5);
        System.arraycopy(r, 1, temp, 6, 5);

        k2[1] = temp[6]; k2[2] = temp[3]; k2[3] = temp[7];
        k2[4] = temp[4]; k2[5] = temp[8]; k2[6] = temp[5];
        k2[7] = temp[10]; k2[8] = temp[9];
    }

    //k是轮密钥，R是输入的右边四位
    //该函数作用是对输入的右半部分进行f函数处理，再将结果存入R中
    public static void f(int[] R, int[] K) {

        //对右边四位进行扩展置换
        int[] temp = new int[9];
        temp[1] = R[4]; temp[2] = R[1]; temp[3] = R[2];
        temp[4] = R[3]; temp[5] = R[2]; temp[6] = R[3];
        temp[7] = R[4]; temp[8] = R[1];

        //扩展后与轮密钥异或操作
        for (int i = 1; i <= 8; i++) {
            temp[i] ^= K[i];//异或运算
        }

        //s1左边四位，s2右边四位
        int[] s1 = new int[5], s2 = new int[5];
        System.arraycopy(temp, 1, s1, 1, 4);
        System.arraycopy(temp, 5, s2, 1, 4);


        int x1 = S1[s1[1] * 2 + s1[4]][s1[2] * 2 + s1[3]][0];
        int x2 = S1[s1[1] * 2 + s1[4]][s1[2] * 2 + s1[3]][1];
        int x3 = S2[s2[1] * 2 + s2[4]][s2[2] * 2 + s2[3]][0];
        int x4 = S2[s2[1] * 2 + s2[4]][s2[2] * 2 + s2[3]][1];

        //直接置换
        R[1] = x2; R[2] = x4; R[3] = x3; R[4] = x1;
    }

    //k1和k2为两个轮密钥
    public static String encode(String x, int[] k1, int[] k2) {
        int[] ming = new int[9];
        for (int i = 1; i <= 8; i++) {
            ming[i] = x.charAt(i - 1) - '0'; // 从字符串中解析每一位
        }

        // 初始置换
        int[] temp = new int[9];
        temp[1] = ming[2]; temp[2] = ming[6]; temp[3] = ming[3];
        temp[4] = ming[1]; temp[5] = ming[4]; temp[6] = ming[8];
        temp[7] = ming[5]; temp[8] = ming[7];

        int[] L0 = new int[5], R0 = new int[5], L1 = new int[5], R1 = new int[5], L2 = new int[5], R2 = new int[5];
        System.arraycopy(temp, 1, L0, 1, 4);
        System.arraycopy(temp, 5, R0, 1, 4);

        // 第一个轮次
        System.arraycopy(R0, 0, L1, 0, R0.length);
        f(R0, k1);
        for (int i = 1; i <= 4; i++) {
            R1[i] = L0[i] ^ R0[i];
        }

        // 第二个轮次
        System.arraycopy(R1, 0, R2, 0, R1.length);
        f(R1, k2);
        for (int i = 1; i <= 4; i++) {
            L2[i] = L1[i] ^ R1[i];
        }

        // 最终置换
        temp[1] = L2[4]; temp[2] = L2[1]; temp[3] = L2[3];
        temp[4] = R2[1]; temp[5] = R2[3]; temp[6] = L2[2];
        temp[7] = R2[4]; temp[8] = R2[2];

        StringBuilder ciphertext = new StringBuilder();
        for (int i = 1; i <= 8; i++) {
            ciphertext.append(temp[i]);
        }

        return ciphertext.toString(); // 返回密文字符串
    }

    public static String decode(String x, int[] k1, int[] k2) {
        int[] ming = new int[9];
        for (int i = 1; i <= 8; i++) {
            ming[i] = x.charAt(i - 1) - '0'; // 从字符串中解析每一位
        }

        int[] temp = new int[9];
        temp[1] = ming[2]; temp[2] = ming[6]; temp[3] = ming[3];
        temp[4] = ming[1]; temp[5] = ming[4]; temp[6] = ming[8];
        temp[7] = ming[5]; temp[8] = ming[7];

        int[] L0 = new int[5], R0 = new int[5], L1 = new int[5], R1 = new int[5], L2 = new int[5], R2 = new int[5];
        System.arraycopy(temp, 1, L2, 1, 4);
        System.arraycopy(temp, 5, R2, 1, 4);

        System.arraycopy(R2, 0, R1, 0, R2.length);
        f(R2, k2);
        for (int i = 1; i <= 4; i++) {
            L1[i] = L2[i] ^ R2[i];
        }

        System.arraycopy(L1, 0, R0, 0, L1.length);
        f(L1, k1);
        for (int i = 1; i <= 4; i++) {
            L0[i] = R1[i] ^ L1[i];
        }

        temp[1] = L0[4]; temp[2] = L0[1]; temp[3] = L0[3];
        temp[4] = R0[1]; temp[5] = R0[3]; temp[6] = L0[2];
        temp[7] = R0[4]; temp[8] = R0[2];

        StringBuilder plaintext = new StringBuilder();
        for (int i = 1; i <= 8; i++) {
            plaintext.append(temp[i]);
        }

        return plaintext.toString(); // 返回明文字符串
    }

    public static String bruteForceSDES(String plaintext, String ciphertext) {
        int[] k1 = new int[9];
        int[] k2 = new int[9];
        StringBuilder foundKeys = new StringBuilder();

        long startTime = System.currentTimeMillis(); // 开始时间

        for (int i = 0; i < 1024; i++) { // 2^10 = 1024
            // 生成 10 位二进制密钥
            int[] k = new int[11];
            for (int a = 0; a < 10; a++) {
                k[a + 1] = (i >> (9 - a)) & 1; // 填充k数组
            }

            // 生成轮密钥
            createKey(k, k1, k2);

            // 尝试用生成的密钥进行加密
            String result = encode(plaintext, k1, k2); // 确保encode方法返回加密结果

            // 如果加密结果等于密文，则找到了密钥
            if (result.equals(ciphertext)) {
                long currentTime = System.currentTimeMillis(); // 当前时间
                long timeSpent = currentTime - startTime; // 计算耗时
                foundKeys.append(intArrayToString(k)).append(" (时间: ").append(timeSpent).append("ms)\n"); // 添加找到的密钥和时间
                //startTime = currentTime; // 重置开始时间
            }
        }
        return foundKeys.length() > 0 ? foundKeys.toString() : null; // 返回找到的密钥或null
    }




    public static String intArrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i <= 10; i++) { // 从索引 1 到 10
            sb.append(arr[i]);
        }
        return sb.toString();
    }

}
