import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author 九天临兵斗
 * @time 2019/6/24
 * @deprecated 下载一组视频，例如效果类似于下载一部电视剧
 */
public class doloadV4 {

    /**
     * 固有不动
     * */
    static String kinleStr[] = {"", ".m3u8.", "-"};
    static String version[] = {"v1", "v2", "v3"};
    static String type[] = {"mp4", "ts", "video"};
    static Integer err = 0;
    static String errInfo = "";

    public static void main(String[] args) throws IOException {

//        Integer m_id = 101;
//        Integer id[] = {800,801,802,803,804,805,806,807};
//        String code[] = {
//                "8e19e9a74b",
//                "09f0cc3018",
//                "1972dd68ca",
//                "6c65444e36",
//                "1ca9b967cd",
//                "5b8e3f76e4",
//                "bbd9366740",
//                "06e2d23287"};
//        Integer indexMax[] = {488,476,490,531,454,367,449,406};

//        Integer m_id = 65;
//        Integer id[] = {595,599,603,605,616,618,621,624,636,642};
//        String code[] = {
//                "05bb34db2b",
//                "394a0f54d8",
//                "60562a980d",
//                "383aee3378",
//                "f99139a9c6",
//                "e25b953b0a",
//                "639ade1142",
//                "da69bd10e9",
//                "444b4b6842",
//                "87cc54900e"};
//
//        Integer indexMax[] = {400,493,462,461,461,458,466,419,501,496};

        Integer m_id = 103;
        Integer id[] = {885, 886, 887, 888};
        String code[] = {
                "7ff7697ce8",
                "3a1e350450",
                "f34c6b9b25",
                "ed77136a83"};

        Integer indexMax[] = {617,623,610,641};

        for(int i = 0; i<id.length; i++ ){
            System.out.println("开启"+code[i]+"任务...");
            System.out.println("任务信息： "+m_id+", "+id[i]+", "+code[i]+", "+indexMax[i]);
            downloadTS("F", "Python\\"+m_id+"\\"+i+"\\"+code[i], m_id, id[i], code[i], 1, indexMax[i], 2);
        }

        System.out.println("当前任务"+m_id+"已经全部完成");

    }

    /**
     * 下载业务
     * @param pan 指定盘符
     * @param path 指定路径
     * @param indexMain 指定主要索引
     * @param indexSub 定位
     * @param code 十六进制数
     * @param number 开始下载的索引
     * @param indexMax 最大数量
     * @param verNum 版本号
     * @throws IOException
     */
    private static void downloadTS(String pan,String path, Integer indexMain,Integer indexSub,String code,Integer number,Integer indexMax, Integer verNum) throws IOException{
        String webURL = ".julyedu.com/"; //指定网站
        String command = ""; //命令
        String resp = ""; //反馈
        createFolder(pan, path);
        Integer n = 0;
        if(number!=0){
            n = number;
        }
        //下载执行体
        for(;n<indexMax+1;n++){
            //判断文件是否存在，即下载中或下载成功
            File fileExists = new File(pan+":\\"+path+"\\"+code + kinleStr[verNum] + num(n)
                    + kinleStr[verNum]
                    + code + kinleStr[verNum] + num(n) +".ts");

            if(noExists(fileExists)){

                String tempZeroNumber = num(n); //初始化 临时变量体零数

                //规则 .m3u8.
                if(kinleStr[verNum].equals(".m3u8.")){
                    if(n==0 || n==number)System.out.println("以\".\"为规则，执行下载体");
                    resp = execCMD("dl "
                            + "--exec \"move {} "
                            +pan+":\\"
                            +path+"\\{}\" http://" + version[verNum] + webURL + type[verNum] + "/"
                            +indexMain+"/"
                            +indexSub+"/"
                            +code+kinleStr[verNum]
                            +n+".ts");
                }

                //规则 -
                if(kinleStr[verNum].equals("-")){
                    if(n==0 || n==number) System.out.println("以\"-\"为规则，执行下载体.(" + (n==0 || n==number) + ")");

                     command = "dl "
                            + "--exec \"move {} "
                            +pan+":\\"
                            +path+"\\{}\" http://" + version[verNum] + webURL + type[verNum] + "/"
                            +indexMain+"/"
                            +indexSub+"/"
                            +code+kinleStr[verNum]
                            +tempZeroNumber+".ts";

                    resp = execCMD("dl "
                            + "--exec \"move {} "
                            +pan+":\\"
                            +path+"\\{}\" http://" + version[verNum] + webURL + type[verNum] + "/"
                            +indexMain+"/"
                            +indexSub+"/"
                            +code+kinleStr[verNum]
                            +tempZeroNumber+".ts");
                }

                System.out.println("下载："+command);

                //判断文件是否存在，即下载中或下载成功
                File fileDownSucc = new File(pan+":\\"+path+"\\"+code + kinleStr[verNum] + tempZeroNumber
                        + kinleStr[verNum]
                        + code + kinleStr[verNum] + tempZeroNumber +".ts");

                if(!fileDownSucc.exists()){
                    System.err.println("正在执行(已执行"+n+"次)，已完成  "
                            +((float)n/indexMax*100)+"%"
                            +", 下载失败："
                            + pan+":\\"+path+"\\"+code + kinleStr[verNum] + tempZeroNumber
                            + kinleStr[verNum]
                            + code + kinleStr[verNum] + tempZeroNumber +".ts");
                    err = err + 1;
                    n--;
                    if(err>=5){
                        System.err.println("下载失败。请检查网络是否畅通或下载地址是否正确!");

                        if(!errInfo.equals(""))
                            errInfo = errInfo + ", ";

                        errInfo = errInfo
                                + "\""  +code
                                + "\":[{\"code\":\"" + code
                                + "\", \"m_id\":\"" + indexMain
                                + "\", \"id\":\"" + indexSub
                                + "\", \"version\":\"" + version[verNum]
                                + "\", \"index\":\"" + n
                                + "}]";

                        break;
                    }
                }else {
                    err = 0;
                    System.out.println("正在执行(已执行" + n + "次)，已完成  " + ((float) n / indexMax * 100) + "%");
                }//判断是否有错误信息
            }//判断文件是否已经存在
        } //循环结束

        if(!(errInfo == "")){
            System.out.println(errInfo);
        }
        System.out.println("下载结束! ");
    }

    /**
     * 文件编号
     * @param n 指定当下索引数
     * @return
     */
    private static String num(int n){
        if(n>999) return "0"+n;
        else if(n>99) return "00"+n;
        else if(n>9) return "000"+n;
        else if(n>-1) return "0000"+n;
        else return "系统异常";
    }

    /**
     *  判断文件是否 不存在
     * @param file 指定文件名
     * @return 如果不在，返回true；如果在，返回false
     */
    private static boolean noExists(File file){
        if(file.exists()){
            System.out.println("该文件已存在，跳过...");
            return false;
        }else{
            return true;
        }
    }

    /**
     * 创建多级文件夹
     * @param folder 指定路径
     * @throws IOException
     */
    private static boolean createFolder(String pan,String folder) throws IOException{
        File file = new File(pan+":\\"+folder);
        System.out.println(pan+":\\"+folder);
        if(!file.exists()){
            //file.mkdir();
            file.mkdirs();
            System.out.println("文件夹"+folder+"创建成功");
            return true;
        }else{
            System.out.println("文件夹"+folder+"已经存在");
            return false;
        }
    }

    /**
     * 创建单个文件夹
     * @param folder 指定文件夹
     * @throws IOException
     */
    private static boolean createFolder(String folder) throws IOException{
        File file = new File("H:\\"+folder);
        System.out.println("H:\\"+folder);
        if(!file.exists()){
            file.mkdir();
            System.out.println("文件夹"+folder+"创建成功");
            return true;
        }else{
            System.out.println("文件夹"+folder+"已经存在");
            return false;
        }
    }

    /**
     * 运行CMD命令
     * @param command DOS命令
     * @return 返回字符串
     */
    private static String execCMD(String command) {
        StringBuilder sb =new StringBuilder();
        try {
            Process process=Runtime.getRuntime().exec(command);
            BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while((line=bufferedReader.readLine())!=null)
            {
                sb.append(line+"\n");
            }
        } catch (Exception e) {
            return e.toString();
        }
        return sb.toString();
    }

    /**
     * 运行CMD命令  GBK编码,可取消乱码问题
     * @param command DOS命令
     * @param encode 输出编码方式GBK
     * @return 反馈信息
     */
    private static String execCMD(String command,String encode) {
        StringBuilder sb =new StringBuilder();
        try {
            // 执行ping命令
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream(), Charset.forName(encode)));
            String line = null;
            while ((line = br.readLine()) != null) {
                //System.out.println(line);
                sb.append(line+"\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}