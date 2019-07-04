import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

/**
 * @author 九天临兵斗
 * @time 2019/7/4
 * @deprecated 下载一组视频，例如效果类似于下载一部电视剧
 *
 *  7/4
 * 1. 使用更加简明。如果发现存在，不在持续判断输出，一直碰到正常运行才继续输出。
 * 2. 可以下载m3u8系列视频
 *
 */
public class doloadV4 {

    /**
     * 固有不动
     * */
    static String kinleStr[] = {".", ".m3u8.", "-"};
    static String version[] = {"v1", "v2", "v3"};
    static String type[] = {"mp4", "ts", "video"};
    static Integer err = 0;
    static String errInfo = "";
    static String curStr = ""; //当前
    static String subStr = ""; //上一次

    public static void main(String[] args) throws IOException {
//        http://v2.julyedu.com/ts/45/227/4ec968c7.m3u8.890.ts
        Integer m_id = 68;
        Integer id[] = {619};
        String code[] = {
                "5bf7d175c9"};

        Integer indexMax[] = {477};

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

                    command ="dl "
                            + "--exec \"move {} "
                            +pan+":\\"
                            +path+"\\{}\" http://" + version[verNum] + webURL + type[verNum] + "/"
                            +indexMain+"/"
                            +indexSub+"/"
                            +code+kinleStr[verNum]
                            +n+".ts";

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

               // System.out.println("下载："+command);

                //判断文件是否存在，即下载中或下载成功
//                File fileDownSucc = new File(pan+":\\"+path+"\\"+code + kinleStr[verNum] + tempZeroNumber
//                            + kinleStr[verNum]
//                            + code + kinleStr[verNum] + tempZeroNumber +".ts");

                File fileDownSucc = new File("");

                if(kinleStr[verNum].equals("-")){
//
//                    System.out.println(pan+":\\"+path+"\\"+code + kinleStr[verNum] + tempZeroNumber
//                            + kinleStr[verNum]
//                            + code + kinleStr[verNum] + tempZeroNumber +".ts");
                    System.out.println("正在判断是否已经存在 ......");
                    fileDownSucc= new File(pan+":\\"+path+"\\"+code + kinleStr[verNum] + tempZeroNumber
                            + kinleStr[verNum]
                            + code + kinleStr[verNum] + tempZeroNumber +".ts");
                }

                if(kinleStr[verNum].equals(".m3u8.")){

//                    System.out.println(pan+":\\"+path+"\\"+code+kinleStr[verNum]+n+".ts");
                    //System.out.println("正在判断是否已经存在 ......");
                    fileDownSucc = new File(pan+":\\"+path+"\\"+code+kinleStr[verNum]+n
                            +"-"+code+kinleStr[verNum]+n+".ts");
//                    fileDownSucc = new File(pan+":\\"+path+"\\"+code + kinleStr[verNum] + tempZeroNumber +".ts");
                }
                if(!fileDownSucc.exists()){
                    //System.out.println("在吗？"+!fileDownSucc.exists());
                    if(kinleStr[verNum].equals("-")){
                        System.err.println("正在执行(已执行"+n+"次)，已完成  "
                                +((float)n/indexMax*100)+"%"
                                +", 下载失败："
                                + pan+":\\"+path+"\\"+code + kinleStr[verNum] + tempZeroNumber
                                + kinleStr[verNum]
                                + code + kinleStr[verNum] + tempZeroNumber +".ts");
                    }
                    if(kinleStr[verNum].equals(".m3u8.")){
                        System.err.println("正在执行(已执行"+n+"次)，已完成  "
                                +((float)n/indexMax*100)+"%"
                                +", 下载失败："
                                + pan+":\\"+path+"\\"+code+kinleStr[verNum]+n+".ts");
                    }

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
                    //System.out.println("在吗？"+!fileDownSucc.exists());
                    err = 0;
                    curStr = "正在执行(已执行" + n + "次)，已完成  " + ((float) n / indexMax * 100) + "%";
                    System.out.println(curStr);
                }//判断是否有错误信息
            }//判断文件是否已经存在
        } //循环结束

        if(!(errInfo == "")){
            System.out.println(errInfo);
        }
        curStr = "";
        subStr = "";
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
            curStr = "该文件已存在，跳过...";
            if(curStr != subStr){
                System.out.println(curStr);
            }
            subStr = curStr;
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