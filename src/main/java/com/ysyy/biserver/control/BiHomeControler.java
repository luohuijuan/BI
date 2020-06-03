package com.ysyy.biserver.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ysyy.biserver.common.ReturnUtils;
import com.ysyy.biserver.common.Validate;
import com.ysyy.biserver.gpmodel.*;
import com.ysyy.biserver.gpserver.impls.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.groupingBy;

@CrossOrigin
@RestController
public class BiHomeControler {
    @Autowired
    private ResDatetotalImpl server;
    @Autowired
    private ResTotalbyo2OdateImpl o2oServer;
    @Autowired
    private ResTotalbyb2CdateImpl b2cServer;
    @Autowired
    private ResTotalbydateareao2OImpl o2oAreaServer;
    @Autowired
    private ResTotalbydateareab2CImpl b2cAreaServer;
    @Autowired
    private GzDimImpl gzDimServer;
    @Autowired
    private ResTotalbydateskuplatImpl skuServer;
    @Autowired
    private ResuserImpl userServer;
    @Autowired
    private DaysellcountImpl dasellServer;
    @Autowired
    private ResTotalbydayareao2OImpl o2oDayServer;

    private Validate v=new Validate();
    private static final DecimalFormat df = new DecimalFormat("0.00");//保留两位小数点
    private static final DecimalFormat df2 = new DecimalFormat("0");//不保留小数点

    /**
     * 累计销售达成
     * @param params
     * @return
     */
    @PostMapping("/gettotalbyct")
    public String GetTotalByct(@RequestBody Map params){
       try{

           JSONObject json=new JSONObject(params);
           json=JSON.parseObject(json.toString());
           if (json != null && json.get("userinfo")!=null) {
               if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                   return ReturnUtils.get2002();
               }
           }
           else {
               return ReturnUtils.get2002();
           }
           //定义返回的json
           JSONObject returnJson=new JSONObject();
           if(json!=null&&json.get("queryinfo")!=null)
           {
               json=JSON.parseObject(json.get("queryinfo").toString());
               if(json.get("year")!=null &&json.get("year").toString().length()>0 ){


                   if(json.get("quarter")!=null &&json.get("quarter").toString().length()>0 ){
                       //季度统计
                       List<ResDatetotal> list=server.GetYearAndQuarterTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()));
                       double gmv=list.stream().mapToDouble(ResDatetotal::getGvm).sum();
                       double ts=list.stream().mapToDouble(ResDatetotal::getTs).sum();
                       long order_count=list.stream().mapToLong(ResDatetotal::getOrdercount).sum();
                       double ml=list.stream().mapToDouble(ResDatetotal::getMl).sum();

                       List<ResDatetotal> listllast=server.GetYearAndQuarterTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString())-1);
                       double gmvlast=listllast.stream().mapToDouble(ResDatetotal::getGvm).sum();
                       double tslast=listllast.stream().mapToDouble(ResDatetotal::getTs).sum();
                       long order_countlast=listllast.stream().mapToLong(ResDatetotal::getOrdercount).sum();
                       double mllast=listllast.stream().mapToDouble(ResDatetotal::getMl).sum();

                       List<ResDatetotal> listltb=server.GetYearAndQuarterTotal(Integer.parseInt(json.get("year").toString())-1,Integer.parseInt(json.get("quarter").toString()));
                       double gmvtb=listltb.stream().mapToDouble(ResDatetotal::getGvm).sum();
                       double tstb=listltb.stream().mapToDouble(ResDatetotal::getTs).sum();
                       long order_counttb=listltb.stream().mapToLong(ResDatetotal::getOrdercount).sum();
                       //毛利总额
                       double mltb=listltb.stream().mapToDouble(ResDatetotal::getMl).sum();

                       //计算季
                       return  "{\n" +
                               "    \"error_code\": 0,\n" +
                               "    \"data\": {\n" +
                               "      \"gmv\": \""+df.format(gmv)+"\",\n" +
                               "      \"ts\": \""+df.format(ts)+"\",\n" +
                               "      \"order_count\": \""+order_count+"\",\n" +
                               "      \"custom_price\": "+df.format(ts/order_count)+" ,\n" +
                               "      \"gross_margin\": \""+df.format(ml/ts*100)+"\",\n" +
                               "      \"xs_rate\": \""+df2.format(gmv/(gmv+300000)*100)+"\",\n" +
                               "      \"ml_rate\": \""+df2.format(ml/(ml+300000)*100)+"\",\n" +
                               "      \"jl_rate\": \""+df2.format(ml/(ml+300000)*100-10)+"\",\n" +
                               "      \"kds_rate\": \""+df2.format(Double.valueOf(order_count)/(order_count+30000)*100)+"\",\n" +
                               "      \"xs_tb\":\""+df2.format((gmv-gmvtb)/gmvtb*100)+"\",\n" +
                               "      \"xs_hb\":\""+df2.format((gmv-gmvlast)/gmvlast*100)+"\",\n" +
                               "      \"ml_tb\":\""+df2.format((ml-mltb)/mltb*100)+"\",\n" +
                               "      \"ml_hb\":\""+df2.format((ml-mllast)/mllast*100)+"\",\n" +
                               "      \"jl_tb\":\""+df2.format((ml-mltb)/mltb*100)+"\",\n" +
                               "      \"jl_hb\":\""+df2.format((ml-mllast)/mllast*100)+"\",\n" +
                               "      \"kds_tb\":\""+df2.format(Double.valueOf(order_count-order_counttb)/order_counttb*100)+"\",\n" +
                               "      \"kds_hb\":\""+df2.format(Double.valueOf(order_count-order_countlast)/order_countlast*100)+"\"\n" +
                               "    }\n" +
                               "  }";
                   }
                   else{
                       if(json.get("month")!=null &&json.get("month").toString().length()>0 )
                       {
                           List<ResDatetotal> list=server.GetYearAndMonthTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()));
                           double gmv=list.stream().mapToDouble(ResDatetotal::getGvm).sum();
                           double ts=list.stream().mapToDouble(ResDatetotal::getTs).sum();
                           long order_count=list.stream().mapToLong(ResDatetotal::getOrdercount).sum();
                           double ml=list.stream().mapToDouble(ResDatetotal::getMl).sum();



                           List<ResDatetotal> listllast=server.GetYearAndMonthTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString())-1);
                           double gmvlast=listllast.stream().mapToDouble(ResDatetotal::getGvm).sum();
                           double tslast=listllast.stream().mapToDouble(ResDatetotal::getTs).sum();
                           long order_countlast=listllast.stream().mapToLong(ResDatetotal::getOrdercount).sum();
                           double mllast=listllast.stream().mapToDouble(ResDatetotal::getMl).sum();

                           List<ResDatetotal> listltb=server.GetYearAndMonthTotal(Integer.parseInt(json.get("year").toString())-1,Integer.parseInt(json.get("month").toString()));
                           double gmvtb=listltb.stream().mapToDouble(ResDatetotal::getGvm).sum();
                           double tstb=listltb.stream().mapToDouble(ResDatetotal::getTs).sum();
                           long order_counttb=listltb.stream().mapToLong(ResDatetotal::getOrdercount).sum();
                           double mltb=listltb.stream().mapToDouble(ResDatetotal::getMl).sum();



                           //计算月
                           return  "{\n" +
                                   "    \"error_code\": 0,\n" +
                                   "    \"data\": {\n" +
                                   "      \"gmv\": \""+df.format(gmv)+"\",\n" +
                                   "      \"ts\": \""+df.format(ts)+"\",\n" +
                                   "      \"order_count\": \""+order_count+"\",\n" +
                                   "      \"custom_price\": "+df.format(ts/order_count)+" ,\n" +
                                   "      \"gross_margin\": \""+df.format(ml/ts*100)+"\",\n" +
                                   "      \"xs_rate\": \""+df2.format(gmv/(gmv+100000)*100)+"\",\n" +
                                   "      \"ml_rate\": \""+df2.format(ml/(ml+100000)*100)+"\",\n" +
                                   "      \"jl_rate\": \""+df2.format(ml/(ml+100000)*100-10)+"\",\n" +
                                   "      \"kds_rate\": \""+df2.format(Double.valueOf(order_count)/(order_count+10000)*100)+"\",\n" +
                                   "      \"xs_tb\":\""+df2.format((gmv-gmvtb)/gmvtb*100)+"\",\n" +
                                   "      \"xs_hb\":\""+df2.format((gmv-gmvlast)/gmvlast*100)+"\",\n" +
                                   "      \"ml_tb\":\""+df2.format((ml-mltb)/mltb*100)+"\",\n" +
                                   "      \"ml_hb\":\""+df2.format((ml-mllast)/mllast*100)+"\",\n" +
                                   "      \"jl_tb\":\""+df2.format((ml-mltb)/mltb*100)+"\",\n" +
                                   "      \"jl_hb\":\""+df2.format((ml-mllast)/mllast*100)+"\",\n" +
                                   "      \"kds_tb\":\""+df2.format(Double.valueOf(order_count-order_counttb)/order_counttb*100)+"\",\n" +
                                   "      \"kds_hb\":\""+df2.format(Double.valueOf(order_count-order_countlast)/order_countlast*100)+"\"\n" +
                                   "    }\n" +
                                   "  }";

                       }else{

                           //计算年度
                           List<ResDatetotal> list=server.GetYearTotal(Integer.parseInt(json.get("year").toString()));

                           List<ResDatetotal> listllast=server.GetYearTotal(Integer.parseInt(json.get("year").toString())-1);
//                           double gvm= list.stream().mapToDouble(ResDatetotal::getGvm).sum();
//                           System.out.println(df.format(list.stream().mapToDouble(ResDatetotal::getGvm).sum()));

                           double gmv=list.stream().mapToDouble(ResDatetotal::getGvm).sum();
                           double ts=list.stream().mapToDouble(ResDatetotal::getTs).sum();
                           long order_count=list.stream().mapToLong(ResDatetotal::getOrdercount).sum();
                           double ml=list.stream().mapToDouble(ResDatetotal::getMl).sum();


                           double gmvlast=listllast.stream().mapToDouble(ResDatetotal::getGvm).sum();
                           double tslast=listllast.stream().mapToDouble(ResDatetotal::getTs).sum();
                           long order_countlast=listllast.stream().mapToLong(ResDatetotal::getOrdercount).sum();
                           double mllast=listllast.stream().mapToDouble(ResDatetotal::getMl).sum();



                           return  "{\n" +
                                   "    \"error_code\": 0,\n" +
                                   "    \"data\": {\n" +
                                   "      \"gmv\": \""+df.format(gmv)+"\",\n" +
                                   "      \"ts\": \""+df.format(ts)+"\",\n" +
                                   "      \"order_count\": \""+order_count+"\",\n" +
                                   "      \"custom_price\": "+df.format(ts/order_count)+" ,\n" +
                                   "      \"gross_margin\": \""+df.format(ml/ts*100)+"\",\n" +
                                   "      \"xs_rate\": \""+df2.format(gmv/(gmv+1000000)*100)+"\",\n" +
                                   "      \"ml_rate\": \""+df2.format(ml/(ml+1000000)*100)+"\",\n" +
                                   "      \"jl_rate\": \""+df2.format(ml/(ml+1000000)*100-10)+"\",\n" +
                                   "      \"kds_rate\": \""+df2.format(Double.valueOf(order_count)/(order_count+100000)*100)+"\",\n" +
                                   "      \"xs_tb\":\"-\",\n" +
                                   "      \"xs_hb\":\""+df2.format((gmv-gmvlast)/gmvlast*100)+"\",\n" +
                                   "      \"ml_tb\":\"-\",\n" +
                                   "      \"ml_hb\":\""+df2.format((ml-mllast)/mllast*100)+"\",\n" +
                                   "      \"jl_tb\":\"-\",\n" +
                                   "      \"jl_hb\":\""+df2.format((ml-mllast)/mllast*100)+"\",\n" +
                                   "      \"kds_tb\":\"-\",\n" +
                                   "      \"kds_hb\":\""+df2.format(Double.valueOf(order_count-order_countlast)/order_countlast*100)+"\"\n" +
                                   "    }\n" +
                                   "  }";

                       }

                   }

               }
               else{
                   return ReturnUtils.get2004();
               }

           }
           else{
               return ReturnUtils.get2004();
           }
       }
       catch (Exception e){
           return ReturnUtils.get2004();

       }

    }

    /**
     * o2o分区目标达成
     * @param params
     * @return
     */
    @PostMapping("/geto2obyct")
    public String GetO2OByct(@RequestBody Map params){
        try{

            JSONObject json=new JSONObject(params);
            json=JSON.parseObject(json.toString());
            if (json != null && json.get("userinfo")!=null) {
                if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                    return ReturnUtils.get2002();
                }
            }
            else {
                return ReturnUtils.get2002();
            }
            //定义返回的json
            JSONObject returnJson=new JSONObject();
            if(json!=null&&json.get("queryinfo")!=null)
            {
                json=JSON.parseObject(json.get("queryinfo").toString());
                if(json.get("year")!=null &&json.get("year").toString().length()>0 ){
                    if(json.get("quarter")!=null &&json.get("quarter").toString().length()>0 ){
                        //计算季
                        List<ResTotalbyo2Odate> list=o2oServer.GetYearAndQuarterTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()));

                        List<ResTotalbyo2Odate> listllast=o2oServer.GetYearAndQuarterTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString())-1);
//                           double gvm= list.stream().mapToDouble(ResDatetotal::getGvm).sum();
//                           System.out.println(df.format(list.stream().mapToDouble(ResDatetotal::getGvm).sum()));

                        double ts=list.stream().mapToDouble(ResTotalbyo2Odate::getTS).sum();

                        long order_count=list.stream().mapToLong(ResTotalbyo2Odate::getOrdercount).sum();


                        double tslast=listllast.stream().mapToDouble(ResTotalbyo2Odate::getTS).sum();

                        List<ResTotalbyo2Odate> listltb=o2oServer.GetYearAndQuarterTotal(Integer.parseInt(json.get("year").toString())-1,Integer.parseInt(json.get("quarter").toString()));
                        double tstb=listltb.stream().mapToDouble(ResTotalbyo2Odate::getTS).sum();

                        return  "{\n" +
                                "    \"error_code\": 0,\n" +
                                "    \"data\": {\n" +
                                "        \"xse\":\""+df.format(ts)+"\",\n" +
                                "        \"rate\":\""+df.format(ts/(ts+120000)*100)+"\",\n" +
                                "        \"tb\":\""+df2.format((ts-tstb)/tstb*100)+"\",\n" +
                                "        \"hb\":\""+df2.format((ts-tslast)/tslast*100)+"\",\n" +
                                "        \"order_count\":\""+order_count+"\",\n" +
                                "        \"custom_price\":\""+df.format(ts/order_count)+"\"\n" +
                                "        }\n" +
                                "  }";
                    }
                    else{
                        if(json.get("month")!=null &&json.get("month").toString().length()>0 )
                        {
                            //计算月
                            List<ResTotalbyo2Odate> list=o2oServer.GetYearAndMonthTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()));
                            List<ResTotalbyo2Odate> listllast=o2oServer.GetYearAndMonthTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString())-1);
                            double ts=list.stream().mapToDouble(ResTotalbyo2Odate::getTS).sum();
                            long order_count=list.stream().mapToLong(ResTotalbyo2Odate::getOrdercount).sum();
                            double tslast=listllast.stream().mapToDouble(ResTotalbyo2Odate::getTS).sum();

                            List<ResTotalbyo2Odate> listltb=o2oServer.GetYearAndMonthTotal(Integer.parseInt(json.get("year").toString())-1,Integer.parseInt(json.get("month").toString()));
                            double tstb=listltb.stream().mapToDouble(ResTotalbyo2Odate::getTS).sum();

                            return  "{\n" +
                                    "    \"error_code\": 0,\n" +
                                    "    \"data\": {\n" +
                                    "        \"xse\":\""+df.format(ts)+"\",\n" +
                                    "        \"rate\":\""+df.format(ts/(ts+50000)*100)+"\",\n" +
                                    "        \"tb\":\""+df2.format((ts-tstb)/tstb*100)+"\",\n" +
                                    "        \"hb\":\""+df2.format((ts-tslast)/tslast*100)+"\",\n" +
                                    "        \"order_count\":\""+order_count+"\",\n" +
                                    "        \"custom_price\":\""+df.format(ts/order_count)+"\"\n" +
                                    "        }\n" +
                                    "  }";

                        }else{
                            //计算年度

                            List<ResTotalbyo2Odate> list=o2oServer.GetYearTotal(Integer.parseInt(json.get("year").toString()));

                            List<ResTotalbyo2Odate> listllast=o2oServer.GetYearTotal(Integer.parseInt(json.get("year").toString())-1);

                            double ts=list.stream().mapToDouble(ResTotalbyo2Odate::getTS).sum();
                            long order_count=list.stream().mapToLong(ResTotalbyo2Odate::getOrdercount).sum();

                            double tslast=listllast.stream().mapToDouble(ResTotalbyo2Odate::getTS).sum();



                            return  "{\n" +
                                    "    \"error_code\": 0,\n" +
                                    "    \"data\": {\n" +
                                    "        \"xse\":\""+df.format(ts)+"\",\n" +
                                    "        \"rate\":\""+df.format(ts/(ts+500000)*100)+"\",\n" +
                                    "        \"tb\":\"-\",\n" +
                                    "        \"hb\":\""+df2.format((ts-tslast)/tslast*100)+"\",\n" +
                                    "        \"order_count\":\""+order_count+"\",\n" +
                                    "        \"custom_price\":\""+df.format(ts/order_count)+"\"\n" +
                                    "        }\n" +
                                    "  }";

                        }

                    }

                }
                else{
                    return ReturnUtils.get2004();
                }

            }
            else{
                return ReturnUtils.get2004();
            }
        }
        catch (Exception e){
            return ReturnUtils.get2004();

        }



    }

    /**
     *b2c平台目标达成
     * @param params
     * @return
     */
    @PostMapping("/getb2cbyct")
    public String GetB2cByCT(@RequestBody Map params){
        try{

            JSONObject json=new JSONObject(params);
            json=JSON.parseObject(json.toString());
            if (json != null && json.get("userinfo")!=null) {
                if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                    return ReturnUtils.get2002();
                }
            }
            else {
                return ReturnUtils.get2002();
            }
            //定义返回的json
            JSONObject returnJson=new JSONObject();
            if(json!=null&&json.get("queryinfo")!=null)
            {
                json=JSON.parseObject(json.get("queryinfo").toString());
                if(json.get("year")!=null &&json.get("year").toString().length()>0 ){
                    if(json.get("quarter")!=null &&json.get("quarter").toString().length()>0 ){
                        //计算季
                        List<ResTotalbyb2Cdate> list=b2cServer.GetYearAndQuarterTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()));
                        List<ResTotalbyb2Cdate> listllast=b2cServer.GetYearAndQuarterTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString())-1);
                        List<ResTotalbyb2Cdate> listltb=b2cServer.GetYearAndQuarterTotal(Integer.parseInt(json.get("year").toString())-1,Integer.parseInt(json.get("quarter").toString()));//同比
                        double ts=list.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                        long order_count=list.stream().mapToLong(ResTotalbyb2Cdate::getOrdercount).sum();
                        double tslast=listllast.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                        double tstb=listltb.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                        List<ResTotalbyb2Cdate> listbtc=b2cServer.GetYearAndQuarterTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()),"b2c");
                        List<ResTotalbyb2Cdate> listllastbtc=b2cServer.GetYearAndQuarterTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString())-1,"b2c");
                        List<ResTotalbyb2Cdate> listltbbtc=b2cServer.GetYearAndQuarterTotalb2c(Integer.parseInt(json.get("year").toString())-1,Integer.parseInt(json.get("quarter").toString()),"b2c");//同比
                        double tsbtc=listbtc.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                        long order_countbtc=listbtc.stream().mapToLong(ResTotalbyb2Cdate::getOrdercount).sum();
                        double tslastbtc=listllastbtc.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                        double tstbbtc=listltbbtc.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                        List<ResTotalbyb2Cdate> listst=b2cServer.GetYearAndQuarterTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()),"手淘");
                        List<ResTotalbyb2Cdate> listllastst=b2cServer.GetYearAndQuarterTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString())-1,"手淘");
                        List<ResTotalbyb2Cdate> liststtbst=b2cServer.GetYearAndQuarterTotalb2c(Integer.parseInt(json.get("year").toString())-1,Integer.parseInt(json.get("quarter").toString()),"手淘");//同比
                        double tsst=listst.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                        long order_countst=listst.stream().mapToLong(ResTotalbyb2Cdate::getOrdercount).sum();
                        double tslastst=listllastst.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                        double tstbst=liststtbst.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                        List<ResTotalbyb2Cdate> listmt=b2cServer.GetYearAndQuarterTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()),"美团");
                        List<ResTotalbyb2Cdate> listllastmt=b2cServer.GetYearAndQuarterTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString())-1,"美团");
                        List<ResTotalbyb2Cdate> listtbmt=b2cServer.GetYearAndQuarterTotalb2c(Integer.parseInt(json.get("year").toString())-1,Integer.parseInt(json.get("quarter").toString()),"美团");
                        double tsmt=listmt.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                        long order_countmt=listmt.stream().mapToLong(ResTotalbyb2Cdate::getOrdercount).sum();
                        double tslastmt=listllastmt.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                        double tstbmt=listtbmt.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();


                        return  "{\n" +
                                "\"error_code\": 0,\n" +
                                "\"data\": {\n" +
                                "        \"xse\":\""+df.format(ts)+"\",\n" +
                                "        \"rate\":\""+df.format(ts/(ts+40000)*100)+"\",\n" +
                                "        \"tb\":\""+df2.format((ts-tstb)/tstb*100)+"\",\n" +
                                "        \"hb\":\""+df2.format((ts-tslast)/tslast*100)+"\",\n" +
                                "        \"order_count\":\""+order_count+"\",\n" +
                                "        \"custom_price\":\""+df.format(ts/order_count)+"\",\n" +
                                "        \"platform_data\": [\n" +
                                "                {\n" +
                                "                \"name\": \"b2c\",\n" +
                                "                \"xse\":\""+df.format(tsbtc)+"\",\n" +
                                "        \"rate\":\""+df.format(tsbtc/(tsbtc+500000)*100)+"\",\n" +
                                "        \"tb\":\""+df2.format((tsbtc-tstbbtc)/tstbbtc*100)+"\",\n" +
                                "        \"hb\":\""+df2.format((tsbtc-tslastbtc)/tslastbtc*100)+"\",\n" +
                                "        \"order_count\":\""+order_count+"\",\n" +
                                "        \"custom_price\":\""+df.format(tsbtc/order_countbtc)+"\"\n" +
                                "                },\n" +
                                "                {\n" +
                                "                \"name\": \"手淘\",\n" +
                                "                \"xse\":\""+df.format(tsst)+"\",\n" +
                                "        \"rate\":\""+df.format(tsst/(tsst+500000)*100)+"\",\n" +
                                "        \"tb\":\""+df2.format((tsst-tstbst)/tstbst*100)+"\",\n" +
                                "        \"hb\":\""+df2.format((tsst-tslastst)/tslastst*100)+"\",\n" +
                                "        \"order_count\":\""+order_count+"\",\n" +
                                "        \"custom_price\":\""+df.format(tsst/order_countst)+"\"\n" +
                                "                },\n" +
                                "                {\n" +
                                "                \"name\": \"美团\",\n" +
                                "                \"xse\":\""+df.format(tsmt)+"\",\n" +
                                "        \"rate\":\""+df.format(tsmt/(tsmt+500000)*100)+"\",\n" +
                                "        \"tb\":\""+df2.format((tsmt-tstbmt)/tstbmt*100)+"\",\n" +
                                "        \"hb\":\""+df2.format((tsmt-tslastmt)/tslastmt*100)+"\",\n" +
                                "        \"order_count\":\""+order_count+"\",\n" +
                                "        \"custom_price\":\""+df.format(tsmt/order_countmt)+"\"\n" +
                                "                }\n" +
                                "]\n" +
                                "}\n" +
                                "}";
                    }
                    else{
                        if(json.get("month")!=null &&json.get("month").toString().length()>0 )
                        {
                            //计算月

                            List<ResTotalbyb2Cdate> list=b2cServer.GetYearAndMonthTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()));
                            List<ResTotalbyb2Cdate> listllast=b2cServer.GetYearAndMonthTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString())-1);
                            List<ResTotalbyb2Cdate> listltb=b2cServer.GetYearAndMonthTotal(Integer.parseInt(json.get("year").toString())-1,Integer.parseInt(json.get("month").toString()));//同比
                            double ts=list.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                            long order_count=list.stream().mapToLong(ResTotalbyb2Cdate::getOrdercount).sum();
                            double tslast=listllast.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                            double tstb=listltb.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                            List<ResTotalbyb2Cdate> listbtc=b2cServer.GetYearAndMonthTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()),"b2c");
                            List<ResTotalbyb2Cdate> listllastbtc=b2cServer.GetYearAndMonthTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString())-1,"b2c");
                            List<ResTotalbyb2Cdate> listltbbtc=b2cServer.GetYearAndMonthTotalb2c(Integer.parseInt(json.get("year").toString())-1,Integer.parseInt(json.get("month").toString()),"b2c");//同比
                            double tsbtc=listbtc.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                            long order_countbtc=listbtc.stream().mapToLong(ResTotalbyb2Cdate::getOrdercount).sum();
                            double tslastbtc=listllastbtc.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                            double tstbbtc=listltbbtc.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                            List<ResTotalbyb2Cdate> listst=b2cServer.GetYearAndMonthTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()),"手淘");
                            List<ResTotalbyb2Cdate> listllastst=b2cServer.GetYearAndMonthTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString())-1,"手淘");
                            List<ResTotalbyb2Cdate> liststtbst=b2cServer.GetYearAndMonthTotalb2c(Integer.parseInt(json.get("year").toString())-1,Integer.parseInt(json.get("month").toString()),"手淘");//同比
                            double tsst=listst.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                            long order_countst=listst.stream().mapToLong(ResTotalbyb2Cdate::getOrdercount).sum();
                            double tslastst=listllastst.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                            double tstbst=liststtbst.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                            List<ResTotalbyb2Cdate> listmt=b2cServer.GetYearAndMonthTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()),"美团");
                            List<ResTotalbyb2Cdate> listllastmt=b2cServer.GetYearAndMonthTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString())-1,"美团");
                            List<ResTotalbyb2Cdate> listtbmt=b2cServer.GetYearAndMonthTotalb2c(Integer.parseInt(json.get("year").toString())-1,Integer.parseInt(json.get("month").toString()),"美团");
                            double tsmt=listmt.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                            long order_countmt=listmt.stream().mapToLong(ResTotalbyb2Cdate::getOrdercount).sum();
                            double tslastmt=listllastmt.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                            double tstbmt=listtbmt.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();


                            return  "{\n" +
                                    "\"error_code\": 0,\n" +
                                    "\"data\": {\n" +
                                    "        \"xse\":\""+df.format(ts)+"\",\n" +
                                    "        \"rate\":\""+df.format(ts/(ts+40000)*100)+"\",\n" +
                                    "        \"tb\":\""+df2.format((ts-tstb)/tstb*100)+"\",\n" +
                                    "        \"hb\":\""+df2.format((ts-tslast)/tslast*100)+"\",\n" +
                                    "        \"order_count\":\""+order_count+"\",\n" +
                                    "        \"custom_price\":\""+df.format(ts/order_count)+"\",\n" +
                                    "        \"platform_data\": [\n" +
                                    "                {\n" +
                                    "                \"name\": \"b2c\",\n" +
                                    "                \"xse\":\""+df.format(tsbtc)+"\",\n" +
                                    "        \"rate\":\""+df.format(tsbtc/(tsbtc+500000)*100)+"\",\n" +
                                    "        \"tb\":\""+df2.format((tsbtc-tstbbtc)/tstbbtc*100)+"\",\n" +
                                    "        \"hb\":\""+df2.format((tsbtc-tslastbtc)/tslastbtc*100)+"\",\n" +
                                    "        \"order_count\":\""+order_count+"\",\n" +
                                    "        \"custom_price\":\""+df.format(tsbtc/order_countbtc)+"\"\n" +
                                    "                },\n" +
                                    "                {\n" +
                                    "                \"name\": \"手淘\",\n" +
                                    "                \"xse\":\""+df.format(tsst)+"\",\n" +
                                    "        \"rate\":\""+df.format(tsst/(tsst+500000)*100)+"\",\n" +
                                    "        \"tb\":\""+df2.format((tsst-tstbst)/tstbst*100)+"\",\n" +
                                    "        \"hb\":\""+df2.format((tsst-tslastst)/tslastst*100)+"\",\n" +
                                    "        \"order_count\":\""+order_count+"\",\n" +
                                    "        \"custom_price\":\""+df.format(tsst/order_countst)+"\"\n" +
                                    "                },\n" +
                                    "                {\n" +
                                    "                \"name\": \"美团\",\n" +
                                    "                \"xse\":\""+df.format(tsmt)+"\",\n" +
                                    "        \"rate\":\""+df.format(tsmt/(tsmt+500000)*100)+"\",\n" +
                                    "        \"tb\":\""+df2.format((tsmt-tstbmt)/tstbmt*100)+"\",\n" +
                                    "        \"hb\":\""+df2.format((tsmt-tslastmt)/tslastmt*100)+"\",\n" +
                                    "        \"order_count\":\""+order_count+"\",\n" +
                                    "        \"custom_price\":\""+df.format(tsmt/order_countmt)+"\"\n" +
                                    "                }\n" +
                                    "]\n" +
                                    "}\n" +
                                    "}";
                        }else{
                            //计算年度

                            List<ResTotalbyb2Cdate> list=b2cServer.GetYearTotal(Integer.parseInt(json.get("year").toString()));
                            List<ResTotalbyb2Cdate> listllast=b2cServer.GetYearTotal(Integer.parseInt(json.get("year").toString())-1);
                            double ts=list.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                            long order_count=list.stream().mapToLong(ResTotalbyb2Cdate::getOrdercount).sum();
                            double tslast=listllast.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                            List<ResTotalbyb2Cdate> listbtc=b2cServer.GetYearTotalb2c(Integer.parseInt(json.get("year").toString()),"b2c");
                            List<ResTotalbyb2Cdate> listllastbtc=b2cServer.GetYearTotalb2c(Integer.parseInt(json.get("year").toString())-1,"b2c");
                            double tsbtc=listbtc.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                            long order_countbtc=listbtc.stream().mapToLong(ResTotalbyb2Cdate::getOrdercount).sum();
                            double tslastbtc=listllastbtc.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                            List<ResTotalbyb2Cdate> listst=b2cServer.GetYearTotalb2c(Integer.parseInt(json.get("year").toString()),"手淘");
                            List<ResTotalbyb2Cdate> listllastst=b2cServer.GetYearTotalb2c(Integer.parseInt(json.get("year").toString())-1,"手淘");
                            double tsst=listst.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                            long order_countst=listst.stream().mapToLong(ResTotalbyb2Cdate::getOrdercount).sum();
                            double tslastst=listllastst.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                            List<ResTotalbyb2Cdate> listmt=b2cServer.GetYearTotalb2c(Integer.parseInt(json.get("year").toString()),"美团");
                            List<ResTotalbyb2Cdate> listllastmt=b2cServer.GetYearTotalb2c(Integer.parseInt(json.get("year").toString())-1,"美团");
                            double tsmt=listmt.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                            long order_countmt=listmt.stream().mapToLong(ResTotalbyb2Cdate::getOrdercount).sum();
                            double tslastmt=listllastmt.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();


                            return  "{\n" +
                                    "\"error_code\": 0,\n" +
                                    "\"data\": {\n" +
                                    "        \"xse\":\""+df.format(ts)+"\",\n" +
                                    "        \"rate\":\""+df.format(ts/(ts+500000)*100)+"\",\n" +
                                    "        \"tb\":\"-\",\n" +
                                    "        \"hb\":\""+df2.format((ts-tslast)/tslast*100)+"\",\n" +
                                    "        \"order_count\":\""+order_count+"\",\n" +
                                    "        \"custom_price\":\""+df.format(ts/order_count)+"\",\n" +
                                    "        \"platform_data\": [\n" +
                                    "                {\n" +
                                    "                \"name\": \"b2c\",\n" +
                                    "                \"xse\":\""+df.format(tsbtc)+"\",\n" +
                                            "        \"rate\":\""+df.format(tsbtc/(tsbtc+500000)*100)+"\",\n" +
                                            "        \"tb\":\"-\",\n" +
                                            "        \"hb\":\""+df2.format((tsbtc-tslastbtc)/tslastbtc*100)+"\",\n" +
                                            "        \"order_count\":\""+order_count+"\",\n" +
                                            "        \"custom_price\":\""+df.format(tsbtc/order_countbtc)+"\"\n" +
                                    "                },\n" +
                                    "                {\n" +
                                    "                \"name\": \"手淘\",\n" +
                                    "                \"xse\":\""+df.format(tsst)+"\",\n" +
                                    "        \"rate\":\""+df.format(tsst/(tsst+500000)*100)+"\",\n" +
                                    "        \"tb\":\"0\",\n" +
                                    "        \"hb\":\""+df2.format((tsst-tslastst)/tslastst*100)+"\",\n" +
                                    "        \"order_count\":\""+order_count+"\",\n" +
                                    "        \"custom_price\":\""+df.format(tsst/order_countst)+"\"\n" +
                                    "                },\n" +
                                    "                {\n" +
                                    "                \"name\": \"美团\",\n" +
                                    "                \"xse\":\""+df.format(tsmt)+"\",\n" +
                                    "        \"rate\":\""+df.format(tsmt/(tsmt+500000)*100)+"\",\n" +
                                    "        \"tb\":\"-\",\n" +
                                    "        \"hb\":\""+df2.format((tsmt-tslastmt)/tslastmt*100)+"\",\n" +
                                    "        \"order_count\":\""+order_count+"\",\n" +
                                    "        \"custom_price\":\""+df.format(tsmt/order_countmt)+"\"\n" +
                                    "                }\n" +
                                                 "]\n" +
                                               "}\n" +
                                    "}";

                        }

                    }

                }
                else{
                    return ReturnUtils.get2004();
                }

            }
            else{
                return ReturnUtils.get2004();
            }
        }
        catch (Exception e){
            return ReturnUtils.get2004();

        }



    }

    /**
     *累计平台销售占比
     * @param params
     * @return
     */
    @PostMapping("/getplatformbyct")
    public String GetPlatformByCT(@RequestBody Map params){
        try{

            JSONObject json=new JSONObject(params);
            json=JSON.parseObject(json.toString());
            if (json != null && json.get("userinfo")!=null) {
                if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                    return ReturnUtils.get2002();
                }
            }
            else {
                return ReturnUtils.get2002();
            }
            //定义返回的json
            JSONObject returnJson=new JSONObject();
            if(json!=null&&json.get("queryinfo")!=null)
            {
                json=JSON.parseObject(json.get("queryinfo").toString());
                if(json.get("year")!=null &&json.get("year").toString().length()>0 ){
                    if(json.get("quarter")!=null &&json.get("quarter").toString().length()>0 ){
                        //计算季
                        List<ResTotalbyb2Cdate> listst=b2cServer.GetYearAndQuarterTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()),"手淘");
                        double tsst=listst.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                        List<ResTotalbyb2Cdate> listmt=b2cServer.GetYearAndQuarterTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()),"美团");
                        double tsmt=listmt.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                        List<ResTotalbyb2Cdate> listjd=b2cServer.GetYearAndQuarterTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()),"京东");
                        double tsjd=listjd.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                        List<ResTotalbyb2Cdate> listeb=b2cServer.GetYearAndQuarterTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()),"饿百");
                        double tseb=listeb.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                        List<ResTotalbyb2Cdate> listtm=b2cServer.GetYearAndQuarterTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()),"天猫");
                        double tstm=listtm.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();



                        return  "{\n" +
                                "\t\"error_code\": 0,\n" +
                                "\t\"data\": [" +
                                "{\n" +
                                "\t\t\"name\": \"手淘\",\n" +
                                "\t\t\"count\": \""+df.format(tsst)+"\"\n" +
                                "\t},\n" +
                                "{\n" +
                                "\t\t\"name\": \"美团\",\n" +
                                "\t\t\"count\": \""+df.format(tsmt)+"\"\n" +
                                "\t},\n" +
                                "{\n" +
                                "\t\t\"name\": \"京东\",\n" +
                                "\t\t\"count\": \""+df.format(tsjd)+"\"\n" +
                                "\t},\n" +
                                "{\n" +
                                "\t\t\"name\": \"饿百\",\n" +
                                "\t\t\"count\": \""+df.format(tseb)+"\"\n" +
                                "\t},\n" +
                                "{\n" +
                                "\t\t\"name\": \"天猫\",\n" +
                                "\t\t\"count\": \""+df.format(tstm)+"\"\n" +
                                "\t}\n" +
                                "]\n" +
                                "}";
                    }
                    else{
                        if(json.get("month")!=null &&json.get("month").toString().length()>0 )
                        {
                            //计算月

                            List<ResTotalbyb2Cdate> listst=b2cServer.GetYearAndMonthTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()),"手淘");
                            double tsst=listst.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                            List<ResTotalbyb2Cdate> listmt=b2cServer.GetYearAndMonthTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()),"美团");
                            double tsmt=listmt.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                            List<ResTotalbyb2Cdate> listjd=b2cServer.GetYearAndMonthTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()),"京东");
                            double tsjd=listjd.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                            List<ResTotalbyb2Cdate> listeb=b2cServer.GetYearAndMonthTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()),"饿百");
                            double tseb=listeb.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                            List<ResTotalbyb2Cdate> listtm=b2cServer.GetYearAndMonthTotalb2c(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()),"天猫");
                            double tstm=listtm.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();



                            return  "{\n" +
                                    "\t\"error_code\": 0,\n" +
                                    "\t\"data\": [" +
                                    "{\n" +
                                    "\t\t\"name\": \"手淘\",\n" +
                                    "\t\t\"count\": \""+df.format(tsst)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"美团\",\n" +
                                    "\t\t\"count\": \""+df.format(tsmt)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"京东\",\n" +
                                    "\t\t\"count\": \""+df.format(tsjd)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"饿百\",\n" +
                                    "\t\t\"count\": \""+df.format(tseb)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"天猫\",\n" +
                                    "\t\t\"count\": \""+df.format(tstm)+"\"\n" +
                                    "\t}\n" +
                                    "]\n" +
                                    "}";
                        }else{
                            //计算年度



                            List<ResTotalbyb2Cdate> listst=b2cServer.GetYearTotalb2c(Integer.parseInt(json.get("year").toString()),"手淘");
                            double tsst=listst.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                            List<ResTotalbyb2Cdate> listmt=b2cServer.GetYearTotalb2c(Integer.parseInt(json.get("year").toString()),"美团");
                            double tsmt=listmt.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                            List<ResTotalbyb2Cdate> listjd=b2cServer.GetYearTotalb2c(Integer.parseInt(json.get("year").toString()),"京东");
                            double tsjd=listjd.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                            List<ResTotalbyb2Cdate> listeb=b2cServer.GetYearTotalb2c(Integer.parseInt(json.get("year").toString()),"饿百");
                            double tseb=listeb.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                            List<ResTotalbyb2Cdate> listtm=b2cServer.GetYearTotalb2c(Integer.parseInt(json.get("year").toString()),"天猫");
                            double tstm=listtm.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();



                            return  "{\n" +
                                    "\t\"error_code\": 0,\n" +
                                    "\t\"data\": [" +
                                    "{\n" +
                                    "\t\t\"name\": \"手淘\",\n" +
                                    "\t\t\"count\": \""+df.format(tsst)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"美团\",\n" +
                                    "\t\t\"count\": \""+df.format(tsmt)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"京东\",\n" +
                                    "\t\t\"count\": \""+df.format(tsjd)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"饿百\",\n" +
                                    "\t\t\"count\": \""+df.format(tseb)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"天猫\",\n" +
                                    "\t\t\"count\": \""+df.format(tstm)+"\"\n" +
                                    "\t}\n" +
                                    "]\n" +
                                    "}";

                        }

                    }

                }
                else{
                    return ReturnUtils.get2004();
                }

            }
            else{
                return ReturnUtils.get2004();
            }
        }
        catch (Exception e){
            return ReturnUtils.get2004();

        }



    }

    /**
     *累计O2O地区销售占比
     * @param params
     * @return
     */
    @PostMapping("/getareao2obyct")
    public String GetAreaO2OByCT(@RequestBody Map params){
        try{

            JSONObject json=new JSONObject(params);
            json=JSON.parseObject(json.toString());
            if (json != null && json.get("userinfo")!=null) {
                if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                    return ReturnUtils.get2002();
                }
            }
            else {
                return ReturnUtils.get2002();
            }
            //定义返回的json
            JSONObject returnJson=new JSONObject();
            if(json!=null&&json.get("queryinfo")!=null)
            {
                json=JSON.parseObject(json.get("queryinfo").toString());
                if(json.get("year")!=null &&json.get("year").toString().length()>0 ){
                    if(json.get("quarter")!=null &&json.get("quarter").toString().length()>0 ){
                        //计算季

                        List<ResTotalbydateareao2O> list=o2oAreaServer.GetYearAndQuarterTotalArea(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()));
                        List<ResTotalbydateareao2O> listGy=list.stream().filter(model -> model.getProvince().equals("贵州")  && model.getCity().equals("贵阳") ).collect(Collectors.toList());
                        List<ResTotalbydateareao2O> listZy=list.stream().filter(model -> model.getProvince().equals("贵州") && model.getCity().equals("遵义") ).collect(Collectors.toList());
                        List<ResTotalbydateareao2O> listLps=list.stream().filter(model -> model.getProvince().equals("贵州") && model.getCity().equals("铜仁") ).collect(Collectors.toList());
                        List<ResTotalbydateareao2O> listBj=list.stream().filter(model -> model.getProvince().equals("贵州") && model.getCity().equals("黔南") ).collect(Collectors.toList());
                        List<ResTotalbydateareao2O> listOther=list.stream().filter(model -> model.getProvince().equals("贵州") && !model.getCity().equals("贵阳") && !model.getCity().equals("遵义") && !model.getCity().equals("铜仁") && !model.getCity().equals("黔南") ).collect(Collectors.toList());

                        double tsGy=listGy.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                        double tsZy=listZy.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                        double tsLps=listLps.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                        double tsBj=listBj.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                        double tsOther=listOther.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();


                        return  "{\n" +
                                "\t\"error_code\": 0,\n" +
                                "\t\"data\": [" +
                                "{\n" +
                                "\t\t\"name\": \"贵阳\",\n" +
                                "\t\t\"count\": \""+df.format(tsGy)+"\"\n" +
                                "\t},\n" +
                                "{\n" +
                                "\t\t\"name\": \"遵义\",\n" +
                                "\t\t\"count\": \""+df.format(tsZy)+"\"\n" +
                                "\t},\n" +
                                "{\n" +
                                "\t\t\"name\": \"铜仁\",\n" +
                                "\t\t\"count\": \""+df.format(tsLps)+"\"\n" +
                                "\t},\n" +
                                "{\n" +
                                "\t\t\"name\": \"黔南\",\n" +
                                "\t\t\"count\": \""+df.format(tsBj)+"\"\n" +
                                "\t},\n" +
                                "{\n" +
                                "\t\t\"name\": \"其他\",\n" +
                                "\t\t\"count\": \""+df.format(tsOther)+"\"\n" +
                                "\t}\n" +
                                "]\n" +
                                "}";
                    }
                    else{
                        if(json.get("month")!=null &&json.get("month").toString().length()>0 )
                        {
                            //计算月


                            List<ResTotalbydateareao2O> list=o2oAreaServer.GetYearAndMonthTotalArea(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()));
                            List<ResTotalbydateareao2O> listGy=list.stream().filter(model -> model.getProvince().equals("贵州")  && model.getCity().equals("贵阳") ).collect(Collectors.toList());
                            List<ResTotalbydateareao2O> listZy=list.stream().filter(model -> model.getProvince().equals("贵州") && model.getCity().equals("遵义") ).collect(Collectors.toList());
                            List<ResTotalbydateareao2O> listLps=list.stream().filter(model -> model.getProvince().equals("贵州") && model.getCity().equals("铜仁") ).collect(Collectors.toList());
                            List<ResTotalbydateareao2O> listBj=list.stream().filter(model -> model.getProvince().equals("贵州") && model.getCity().equals("黔南") ).collect(Collectors.toList());
                            List<ResTotalbydateareao2O> listOther=list.stream().filter(model -> model.getProvince().equals("贵州") && !model.getCity().equals("贵阳") && !model.getCity().equals("遵义") && !model.getCity().equals("铜仁") && !model.getCity().equals("黔南") ).collect(Collectors.toList());

                            double tsGy=listGy.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                            double tsZy=listZy.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                            double tsLps=listLps.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                            double tsBj=listBj.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                            double tsOther=listOther.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();


                            return  "{\n" +
                                    "\t\"error_code\": 0,\n" +
                                    "\t\"data\": [" +
                                    "{\n" +
                                    "\t\t\"name\": \"贵阳\",\n" +
                                    "\t\t\"count\": \""+df.format(tsGy)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"遵义\",\n" +
                                    "\t\t\"count\": \""+df.format(tsZy)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"铜仁\",\n" +
                                    "\t\t\"count\": \""+df.format(tsLps)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"黔南\",\n" +
                                    "\t\t\"count\": \""+df.format(tsBj)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"其他\",\n" +
                                    "\t\t\"count\": \""+df.format(tsOther)+"\"\n" +
                                    "\t}\n" +
                                    "]\n" +
                                    "}";
                        }else{
                            //计算年度

                            List<ResTotalbydateareao2O> list=o2oAreaServer.GetYearTotalArea(Integer.parseInt(json.get("year").toString()));
                            List<ResTotalbydateareao2O> listGy=list.stream().filter(model -> model.getProvince().equals("贵州")  && model.getCity().equals("贵阳") ).collect(Collectors.toList());
                            List<ResTotalbydateareao2O> listZy=list.stream().filter(model -> model.getProvince().equals("贵州") && model.getCity().equals("遵义") ).collect(Collectors.toList());
                            List<ResTotalbydateareao2O> listLps=list.stream().filter(model -> model.getProvince().equals("贵州") && model.getCity().equals("铜仁") ).collect(Collectors.toList());
                            List<ResTotalbydateareao2O> listBj=list.stream().filter(model -> model.getProvince().equals("贵州") && model.getCity().equals("黔南") ).collect(Collectors.toList());
                            List<ResTotalbydateareao2O> listOther=list.stream().filter(model -> model.getProvince().equals("贵州") && !model.getCity().equals("贵阳") && !model.getCity().equals("遵义") && !model.getCity().equals("铜仁") && !model.getCity().equals("黔南") ).collect(Collectors.toList());

                            double tsGy=listGy.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                            double tsZy=listZy.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                            double tsLps=listLps.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                            double tsBj=listBj.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                            double tsOther=listOther.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();


                            return  "{\n" +
                                    "\t\"error_code\": 0,\n" +
                                    "\t\"data\": [" +
                                    "{\n" +
                                    "\t\t\"name\": \"贵阳\",\n" +
                                    "\t\t\"count\": \""+df.format(tsGy)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"遵义\",\n" +
                                    "\t\t\"count\": \""+df.format(tsZy)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"铜仁\",\n" +
                                    "\t\t\"count\": \""+df.format(tsLps)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"黔南\",\n" +
                                    "\t\t\"count\": \""+df.format(tsBj)+"\"\n" +
                                    "\t},\n" +
                                    "{\n" +
                                    "\t\t\"name\": \"其他\",\n" +
                                    "\t\t\"count\": \""+df.format(tsOther)+"\"\n" +
                                    "\t}\n" +
                                    "]\n" +
                                    "}";

                        }

                    }

                }
                else{
                    return ReturnUtils.get2004();
                }

            }
            else{
                return ReturnUtils.get2004();
            }
        }
        catch (Exception e){
            return ReturnUtils.get2004();

        }
    }

    /**
     * 地图模式
     * @param params
     * @return
     */
    @PostMapping("/getareabyct")
    public String GetAreaByCT(@RequestBody Map params){
        try{

            JSONObject json=new JSONObject(params);
            json=JSON.parseObject(json.toString());
            if (json != null && json.get("userinfo")!=null) {
                if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                    return ReturnUtils.get2002();
                }
            }
            else {
                return ReturnUtils.get2002();
            }
            //定义返回的json
            JSONObject returnJson=new JSONObject();
            if(json!=null&&json.get("queryinfo")!=null)
            {
                json=JSON.parseObject(json.get("queryinfo").toString());
                if(json.get("year")!=null &&json.get("year").toString().length()>0 ){
                    if(json.get("quarter")!=null &&json.get("quarter").toString().length()>0 ){
                        //计算季

                        String returnString="";

                        returnString+="{\n" +
                                "\t\"error_code\": 0,\n" +
                                "\t\"data\": [";

                        List<ResTotalbydateareao2O> listO2O=o2oAreaServer.GetYearAndQuarterTotalArea(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()));
                        List<ResTotalbydateareab2C> listB2C=b2cAreaServer.GetYearAndQuarterTotalArea(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()));
                        List<GzDim> listGz=gzDimServer.GetList();
                        //获取城市
                        if(json.get("c")!=null &&json.get("c").toString().length()>0)
                        {
                            String city=json.get("c").toString();
                            //区县列表
                            List<GzDim> listGzArea=listGz.stream().filter(model -> model.getProvince().equals("贵州")  && model.getCity().equals(city)).collect(Collectors.toList());
                            for(GzDim area:listGzArea){
                                List<ResTotalbydateareao2O> listO2OSup=listO2O.stream().filter(model -> model.getProvince().equals("贵州")  && model.getArea().equals(area.getArea()) ).collect(Collectors.toList());
                                List<ResTotalbydateareab2C> listB2CSup=listB2C.stream().filter(model -> model.getProvince().equals("贵州")  && model.getArea().equals(area.getArea()) ).collect(Collectors.toList());
                                returnString+="{\n" +
                                        "\t\t\"name\": \""+area.getArea()+"\",\n" +
                                        "\t\t\"count\": \""+df.format(listO2OSup.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum()+listB2CSup.stream().mapToDouble(ResTotalbydateareab2C::getTs).sum())+"\"\n" +
                                        "\t},\n";
                            }
                            returnString=returnString.substring(0,returnString.length()-2)+"]\n" +
                                    "}";

                        }
                        else{
                            Map<String, List<GzDim>> employeesByCity = listGz.stream().collect(groupingBy(GzDim::getCity));
                            for (Map.Entry<String, List<GzDim>> entry : employeesByCity.entrySet()) {

                                List<ResTotalbydateareao2O> listO2OSup=listO2O.stream().filter(model -> model.getProvince().equals("贵州")  && model.getCity().equals(entry.getKey()) ).collect(Collectors.toList());
                                List<ResTotalbydateareab2C> listB2CSup=listB2C.stream().filter(model -> model.getProvince().equals("贵州")  && model.getCity().equals(entry.getKey()) ).collect(Collectors.toList());
                                returnString+="{\n" +
                                        "\t\t\"name\": \""+entry.getKey()+"\",\n" +
                                        "\t\t\"count\": \""+df.format(listO2OSup.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum()+listB2CSup.stream().mapToDouble(ResTotalbydateareab2C::getTs).sum())+"\"\n" +
                                        "\t},\n";
                            }
                            returnString=returnString.substring(0,returnString.length()-2)+"]\n" +
                                    "}";

                        }

                        return returnString;
                    }
                    else{
                        if(json.get("month")!=null &&json.get("month").toString().length()>0 )
                        {
                            //计算月


                           String returnString="";

                            returnString+="{\n" +
                                    "\t\"error_code\": 0,\n" +
                                    "\t\"data\": [";

                            List<ResTotalbydateareao2O> listO2O=o2oAreaServer.GetYearAndMonthTotalArea(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()));
                            List<ResTotalbydateareab2C> listB2C=b2cAreaServer.GetYearAndMonthTotalArea(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()));
                            List<GzDim> listGz=gzDimServer.GetList();
                            //获取城市
                            if(json.get("c")!=null &&json.get("c").toString().length()>0)
                            {
                                String city=json.get("c").toString();
                                //区县列表
                                List<GzDim> listGzArea=listGz.stream().filter(model -> model.getProvince().equals("贵州")  && model.getCity().equals(city)).collect(Collectors.toList());
                                for(GzDim area:listGzArea){
                                    List<ResTotalbydateareao2O> listO2OSup=listO2O.stream().filter(model -> model.getProvince().equals("贵州")  && model.getArea().equals(area.getArea()) ).collect(Collectors.toList());
                                    List<ResTotalbydateareab2C> listB2CSup=listB2C.stream().filter(model -> model.getProvince().equals("贵州")  && model.getArea().equals(area.getArea()) ).collect(Collectors.toList());
                                    returnString+="{\n" +
                                            "\t\t\"name\": \""+area.getArea()+"\",\n" +
                                            "\t\t\"count\": \""+df.format(listO2OSup.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum()+listB2CSup.stream().mapToDouble(ResTotalbydateareab2C::getTs).sum())+"\"\n" +
                                            "\t},\n";
                                }
                                returnString=returnString.substring(0,returnString.length()-2)+"]\n" +
                                        "}";

                            }
                            else{
                                Map<String, List<GzDim>> employeesByCity = listGz.stream().collect(groupingBy(GzDim::getCity));
                                for (Map.Entry<String, List<GzDim>> entry : employeesByCity.entrySet()) {

                                    List<ResTotalbydateareao2O> listO2OSup=listO2O.stream().filter(model -> model.getProvince().equals("贵州")  && model.getCity().equals(entry.getKey()) ).collect(Collectors.toList());
                                    List<ResTotalbydateareab2C> listB2CSup=listB2C.stream().filter(model -> model.getProvince().equals("贵州")  && model.getCity().equals(entry.getKey()) ).collect(Collectors.toList());
                                    returnString+="{\n" +
                                            "\t\t\"name\": \""+entry.getKey()+"\",\n" +
                                            "\t\t\"count\": \""+df.format(listO2OSup.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum()+listB2CSup.stream().mapToDouble(ResTotalbydateareab2C::getTs).sum())+"\"\n" +
                                            "\t},\n";
                                }
                                returnString=returnString.substring(0,returnString.length()-2)+"]\n" +
                                        "}";

                            }

                            return returnString;
                        }else{
                            //计算年度

                            String returnString="";

                            returnString+="{\n" +
                                    "\t\"error_code\": 0,\n" +
                                    "\t\"data\": [";

                            List<ResTotalbydateareao2O> listO2O=o2oAreaServer.GetYearTotalArea(Integer.parseInt(json.get("year").toString()));
                            List<ResTotalbydateareab2C> listB2C=b2cAreaServer.GetYearTotalArea(Integer.parseInt(json.get("year").toString()));
                            List<GzDim> listGz=gzDimServer.GetList();
                            //获取城市
                            if(json.get("c")!=null &&json.get("c").toString().length()>0)
                            {
                                String city=json.get("c").toString();
                                //区县列表
                                List<GzDim> listGzArea=listGz.stream().filter(model -> model.getProvince().equals("贵州")  && model.getCity().equals(city)).collect(Collectors.toList());
                                for(GzDim area:listGzArea){
                                    List<ResTotalbydateareao2O> listO2OSup=listO2O.stream().filter(model -> model.getProvince().equals("贵州")  && model.getArea().equals(area.getArea()) ).collect(Collectors.toList());
                                    List<ResTotalbydateareab2C> listB2CSup=listB2C.stream().filter(model -> model.getProvince().equals("贵州")  && model.getArea().equals(area.getArea()) ).collect(Collectors.toList());
                                    returnString+="{\n" +
                                            "\t\t\"name\": \""+area.getArea()+"\",\n" +
                                            "\t\t\"count\": \""+df.format(listO2OSup.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum()+listB2CSup.stream().mapToDouble(ResTotalbydateareab2C::getTs).sum())+"\"\n" +
                                            "\t},\n";
                                }
                                returnString=returnString.substring(0,returnString.length()-2)+"]\n" +
                                        "}";

                            }
                            else{
                                Map<String, List<GzDim>> employeesByCity = listGz.stream().collect(groupingBy(GzDim::getCity));
                                for (Map.Entry<String, List<GzDim>> entry : employeesByCity.entrySet()) {

                                    List<ResTotalbydateareao2O> listO2OSup=listO2O.stream().filter(model -> model.getProvince().equals("贵州")  && model.getCity().equals(entry.getKey()) ).collect(Collectors.toList());
                                    List<ResTotalbydateareab2C> listB2CSup=listB2C.stream().filter(model -> model.getProvince().equals("贵州")  && model.getCity().equals(entry.getKey()) ).collect(Collectors.toList());
                                    returnString+="{\n" +
                                            "\t\t\"name\": \""+entry.getKey()+"\",\n" +
                                            "\t\t\"count\": \""+df.format(listO2OSup.stream().mapToDouble(ResTotalbydateareao2O::getTs).sum()+listB2CSup.stream().mapToDouble(ResTotalbydateareab2C::getTs).sum())+"\"\n" +
                                            "\t},\n";
                                }
                                returnString=returnString.substring(0,returnString.length()-2)+"]\n" +
                                        "}";

                            }

                            return returnString;
                        }
                    }
                }
                else{
                    return ReturnUtils.get2004();
                }

            }
            else{
                return ReturnUtils.get2004();
            }
        }
        catch (Exception e){
            return ReturnUtils.get2004();

        }



    }

    /**
     * 地图模式实时
     * @param params
     * @return
     */
    @PostMapping("/getareabyday")
    public String GetAreaByDay(@RequestBody Map params){
        try{

            JSONObject json=new JSONObject(params);
            json=JSON.parseObject(json.toString());
            if (json != null && json.get("userinfo")!=null) {
                if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                    return ReturnUtils.get2002();
                }
            }
            else {
                return ReturnUtils.get2002();
            }
            //定义返回的json
            JSONObject returnJson=new JSONObject();
            if(json!=null&&json.get("queryinfo")!=null)
            {
                json=JSON.parseObject(json.get("queryinfo").toString());
                String returnString="";

                returnString+="{\n" +
                        "\t\"error_code\": 0,\n" +
                        "\t\"data\": [";

                List<ResTotalbydayareao2O> listO2O=o2oDayServer.GetList();

                List<GzDim> listGz=gzDimServer.GetList();
                //获取城市
                if(json.get("c")!=null &&json.get("c").toString().length()>0)
                {
                    String city=json.get("c").toString();
                    //区县列表
                    List<GzDim> listGzArea=listGz.stream().filter(model -> model.getProvince().equals("贵州")  && model.getCity().equals(city)).collect(Collectors.toList());
                    for(GzDim area:listGzArea){
                        List<ResTotalbydayareao2O> listO2OSup=listO2O.stream().filter(model -> model.getProvince().equals("贵州")  && model.getArea().equals(area.getArea()) ).collect(Collectors.toList());

                        returnString+="{\n" +
                                "\t\t\"name\": \""+area.getArea()+"\",\n" +
                                "\t\t\"count\": \""+df.format(listO2OSup.stream().mapToDouble(ResTotalbydayareao2O::getTs).sum())+"\"\n" +
                                "\t},\n";
                    }
                    returnString=returnString.substring(0,returnString.length()-2)+"]\n" +
                            "}";

                }
                else{
                    Map<String, List<GzDim>> employeesByCity = listGz.stream().collect(groupingBy(GzDim::getCity));
                    for (Map.Entry<String, List<GzDim>> entry : employeesByCity.entrySet()) {

                        List<ResTotalbydayareao2O> listO2OSup=listO2O.stream().filter(model -> model.getProvince().equals("贵州")  && model.getCity().equals(entry.getKey()) ).collect(Collectors.toList());

                        returnString+="{\n" +
                                "\t\t\"name\": \""+entry.getKey()+"\",\n" +
                                "\t\t\"count\": \""+df.format(listO2OSup.stream().mapToDouble(ResTotalbydayareao2O::getTs).sum())+"\"\n" +
                                "\t},\n";
                    }
                    returnString=returnString.substring(0,returnString.length()-2)+"]\n" +
                            "}";

                }

                return returnString;

            }
            else{
                return ReturnUtils.get2004();
            }
        }
        catch (Exception e){
            return ReturnUtils.get2004();

        }



    }

    /**
     * 获取sku商品排行
     * @param params
     * @return
     */
    @PostMapping("/getskutop")
    public String GetSkuTop(@RequestBody Map params){
        try{

            JSONObject json=new JSONObject(params);
            json=JSON.parseObject(json.toString());
            if (json != null && json.get("userinfo")!=null) {
                if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                    return ReturnUtils.get2002();
                }
            }
            else {
                return ReturnUtils.get2002();
            }
            //定义返回的json
            JSONObject returnJson=new JSONObject();
            if(json!=null&&json.get("queryinfo")!=null)
            {
                json=JSON.parseObject(json.get("queryinfo").toString());
                if(json.get("year")!=null &&json.get("year").toString().length()>0 ){
                    if(json.get("quarter")!=null &&json.get("quarter").toString().length()>0 ){
                        //计算季
                        String returnString = "";


                        returnString += "{\n" +
                                "\t\"error_code\": 0,\n" +
                                "\t\"data\": [";

                        List<ResTotalbydateskuplat> listsku = skuServer.GetYearAndQuarterTotalArea(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()));

                        if (json.get("plat") != null && json.get("plat").toString().length() > 0) {//选择平台
                            String plat = json.get("plat").toString();
                            listsku = listsku.stream().filter(model -> model.getPId().equals(plat)).collect(Collectors.toList());

                        } else {

                        }
                        listsku = listsku.stream().filter(model -> model.getHname() != null).collect(Collectors.toList());
                        //未选择平台
                        //分组求和
                        Map<String, Double> map = listsku.stream().collect(Collectors.groupingBy(ResTotalbydateskuplat::getHname, Collectors.summingDouble(ResTotalbydateskuplat::getTs)));

                        // 按值排序降序
                        Map<String, Double> sorted = map
                                .entrySet()
                                .stream()
                                .sorted(Collections.reverseOrder(comparingByValue()))
                                .collect(
                                        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                                LinkedHashMap::new));
                        int i = 0;
                        for (Map.Entry<String, Double> entry : sorted.entrySet()) {
                            if (i < 10) {
                                returnString += "{\n" +
                                        "\t\t\"name\": \"" + entry.getKey() + "\",\n" +
                                        "\t\t\"count\": \"" + df.format(entry.getValue()) + "\"\n" +
                                        "\t},\n";
                            } else {
                                break;
                            }
                            i++;
                        }
                        returnString = returnString.substring(0, returnString.length() - 2) + "]\n" +
                                "}";

                        return returnString;

                    }
                    else{
                        if(json.get("month")!=null &&json.get("month").toString().length()>0 )
                        {
                            //计算月

                            String returnString = "";


                            returnString += "{\n" +
                                    "\t\"error_code\": 0,\n" +
                                    "\t\"data\": [";

                            List<ResTotalbydateskuplat> listsku = skuServer.GetYearAndMonthTotalArea(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()));

                            if (json.get("plat") != null && json.get("plat").toString().length() > 0) {//选择平台
                                String plat = json.get("plat").toString();
                                listsku = listsku.stream().filter(model -> model.getPId().equals(plat)).collect(Collectors.toList());

                            } else {

                            }
                            listsku = listsku.stream().filter(model -> model.getHname() != null).collect(Collectors.toList());
                            //未选择平台
                            //分组求和
                            Map<String, Double> map = listsku.stream().collect(Collectors.groupingBy(ResTotalbydateskuplat::getHname, Collectors.summingDouble(ResTotalbydateskuplat::getTs)));

                            // 按值排序降序
                            Map<String, Double> sorted = map
                                    .entrySet()
                                    .stream()
                                    .sorted(Collections.reverseOrder(comparingByValue()))
                                    .collect(
                                            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                                    LinkedHashMap::new));
                            int i = 0;
                            for (Map.Entry<String, Double> entry : sorted.entrySet()) {
                                if (i < 10) {
                                    returnString += "{\n" +
                                            "\t\t\"name\": \"" + entry.getKey() + "\",\n" +
                                            "\t\t\"count\": \"" + df.format(entry.getValue()) + "\"\n" +
                                            "\t},\n";
                                } else {
                                    break;
                                }
                                i++;
                            }
                            returnString = returnString.substring(0, returnString.length() - 2) + "]\n" +
                                    "}";

                            return returnString;



                        }else {
                            //计算年度

                            String returnString = "";


                            returnString += "{\n" +
                                    "\t\"error_code\": 0,\n" +
                                    "\t\"data\": [";

                            List<ResTotalbydateskuplat> listsku = skuServer.GetYearTotalArea(Integer.parseInt(json.get("year").toString()));

                            if (json.get("plat") != null && json.get("plat").toString().length() > 0) {//选择平台
                                String plat = json.get("plat").toString();
                                listsku = listsku.stream().filter(model -> model.getPId().equals(plat)).collect(Collectors.toList());

                            } else {

                            }
                            listsku = listsku.stream().filter(model -> model.getHname() != null).collect(Collectors.toList());
                            //未选择平台
                            //分组求和
                            Map<String, Double> map = listsku.stream().collect(Collectors.groupingBy(ResTotalbydateskuplat::getHname, Collectors.summingDouble(ResTotalbydateskuplat::getTs)));

                            // 按值排序降序
                            Map<String, Double> sorted = map
                                    .entrySet()
                                    .stream()
                                    .sorted(Collections.reverseOrder(comparingByValue()))
                                    .collect(
                                            Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                                    LinkedHashMap::new));
                            int i = 0;
                            for (Map.Entry<String, Double> entry : sorted.entrySet()) {
                                if (i < 10) {
                                    returnString += "{\n" +
                                            "\t\t\"name\": \"" + entry.getKey() + "\",\n" +
                                            "\t\t\"count\": \"" + df.format(entry.getValue()) + "\"\n" +
                                            "\t},\n";
                                } else {
                                    break;
                                }
                                i++;
                            }
                            returnString = returnString.substring(0, returnString.length() - 2) + "]\n" +
                                    "}";

                            return returnString;
                        }
                    }
                }
                else{
                    return ReturnUtils.get2004();
                }

            }
            else{
                return ReturnUtils.get2004();
            }
        }
        catch (Exception e){
            return ReturnUtils.get2004();

        }


    }

    /**
     * 获取自营平台分析结果
     * @param params
     * @return
     */
    @PostMapping("/getuserfx")
    public String GetUserFx(@RequestBody Map params){

        try{

            JSONObject json=new JSONObject(params);
            json=JSON.parseObject(json.toString());
            if (json != null && json.get("userinfo")!=null) {
                if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                    return ReturnUtils.get2002();
                }
            }
            else {
                return ReturnUtils.get2002();
            }
            //定义返回的json
            JSONObject returnJson=new JSONObject();
            if(json!=null&&json.get("queryinfo")!=null)
            {
                json=JSON.parseObject(json.get("queryinfo").toString());

                   List<Resuser> list =userServer.GetList();
                    String returnString = "";


                    returnString += "{\n" +
                            "\t\"error_code\": 0,\n" +
                            "\t\"data\": [";

                    for (Resuser model : list) {

                        long cshy=model.getTotal()-model.getCshy();
                        returnString += "{\n" +
                                "\t\t\"total\": \"" + model.getTotal()+ "\",\n" +
                                "\t\t\"hyhy\": \"" + model.getHyhy()+ "\",\n" +
                                "\t\t\"xzhy\": \"" + model.getXzhy()+ "\",\n" +
                                "\t\t\"yzhy\": \"" + model.getYzhy()+ "\",\n" +
                                "\t\t\"cshy\": \"" + cshy+ "\",\n" +
                                "\t\t\"hxhy\": \"" + model.getHxhy() + "\"\n" +
                                "\t},\n";
                    }
                    returnString = returnString.substring(0, returnString.length() - 2) + "]\n" +
                            "}";

                    return returnString;


            }
            else{
                return ReturnUtils.get2004();
            }
        }
        catch (Exception e){
            return ReturnUtils.get2004();

        }

    }


    /**
     * 获取日销售数据
     * @param params
     * @return
     */
    @PostMapping("/getdaysellcount")
    public String GetDaySellCount(@RequestBody Map params){

        try{

            JSONObject json=new JSONObject(params);
            json=JSON.parseObject(json.toString());
            if (json != null && json.get("userinfo")!=null) {
                if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                    return ReturnUtils.get2002();
                }
            }
            else {
                return ReturnUtils.get2002();
            }
            //定义返回的json
            JSONObject returnJson=new JSONObject();
            if(json!=null&&json.get("queryinfo")!=null)
            {
                json=JSON.parseObject(json.get("queryinfo").toString());

                List<Daysellcount> list =dasellServer.GetList();
                String returnString = "";


                returnString += "{\n" +
                        "\t\"error_code\": 0,\n" +
                        "\t\"data\": [";

                for (Daysellcount model : list) {

                    returnString += "{\n" +
                            "\t\t\"ts\": \"" + model.getTs()+ "\",\n" +
                            "\t\t\"ordercount\": \"" + model.getOrdercount()+ "\",\n" +
                            "\t\t\"platid\": \"" + model.getPaltid()+ "\",\n" +
                            "\t\t\"paltname\": \"" + model.getPlatname()+ "\"\n" +
                            "\t},\n";
                }
                returnString = returnString.substring(0, returnString.length() - 2) + "]\n" +
                        "}";

                return returnString;


            }
            else{
                return ReturnUtils.get2004();
            }
        }
        catch (Exception e){
            return ReturnUtils.get2004();

        }

    }

    /**
     *累计销售达成下钻页
     */
    @PostMapping("/gettotalselldetails")
    public String GetTotalSellDetails(@RequestBody Map params){

        try{

            JSONObject json=new JSONObject(params);
            json=JSON.parseObject(json.toString());
            if (json != null && json.get("userinfo")!=null) {
                if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                    return ReturnUtils.get2002();
                }
            }
            else {
                return ReturnUtils.get2002();
            }
            //定义返回的json
            JSONObject returnJson=new JSONObject();
            if(json!=null&&json.get("queryinfo")!=null)
            {
                json=JSON.parseObject(json.get("queryinfo").toString());
                if(json.get("year")!=null &&json.get("year").toString().length()>0 ){
                    if(json.get("quarter")!=null &&json.get("quarter").toString().length()>0 ){
                        //计算季
                        String returnString = "";


                        returnString += "{\n" +
                                "\t\"error_code\": 0,\n" +
                                "\t\"data\": [";

                        List<ResDatetotal> list = server.GetYearAndQuarterTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()));

                        if (json.get("plat") != null && json.get("plat").toString().length() > 0) {//选择平台
                            String plat = json.get("plat").toString();
                            list = list.stream().filter(model -> model.getPlat_id().equals(plat)).collect(Collectors.toList());

                        } else {

                        }

                        //安平台分组
                        Map<String, List<ResDatetotal>> map = list.stream().collect(Collectors.groupingBy(ResDatetotal::getPlat_name));
                        int i = 1;
                        for (Map.Entry<String, List<ResDatetotal>> entry : map.entrySet()) {
                            double gmv=entry.getValue().stream().mapToDouble(ResDatetotal::getGvm).sum();
                            double tsxs=entry.getValue().stream().mapToDouble(ResDatetotal::getTs).sum();
                            double tsmb=tsxs+100000;
                            double tsxx=0;
                            double tsdc=(tsxs+tsxx)/tsmb*100;
                            double ml=entry.getValue().stream().mapToDouble(ResDatetotal::getMl).sum();
                            double mlmb=ml+100000;
                            long order_count=entry.getValue().stream().mapToLong(ResDatetotal::getOrdercount).sum();
                            long order_countmb=order_count+100000;
                            returnString += "{\n" +
                                    "\t\t\"xh\": \"" + i+ "\",\n" +
                                    "\t\t\"gvm\": \"" + df.format(gmv) + "\",\n" +
                                    "\t\t\"tsmb\": \"" + df.format(tsmb) + "\",\n" +
                                    "\t\t\"tsxs\": \"" + df.format(tsxs) + "\",\n" +
                                    "\t\t\"tsdc\": \"" + df.format(tsdc) + "%\",\n" +
                                    "\t\t\"tsxx\": \"" + df.format(tsxx) + "\",\n" +
                                    "\t\t\"mlmb\": \"" + df.format(mlmb) + "\",\n" +
                                    "\t\t\"ml\": \"" + df.format(ml) + "\",\n" +
                                    "\t\t\"mldc\": \"" + df.format(ml/mlmb*100) + "%\",\n" +
                                    "\t\t\"order_countmb\": \"" + order_countmb+ "\",\n" +
                                    "\t\t\"order_count\": \"" +order_count + "\",\n" +
                                    "\t\t\"orderdc\": \"" + df.format(order_count/(order_countmb)*100) + "\",\n" +
                                    "\t\t\"plat\": \"" + entry.getKey()+ "\",\n" +
                                    "\t\t\"platgmv\": \"" + df.format(gmv) + "\"\n" +
                                    "\t},\n";

                            i++;
                        }

                        returnString = returnString.substring(0, returnString.length() - 2) + "]\n" +
                                "}";

                        return returnString;


                    }
                    else{
                        if(json.get("months")!=null &&json.get("months").toString().length()>0 )
                        {
                            //计算月

                            String[] strings=json.get("months").toString().split(",");
                            List<Integer> months=new ArrayList<>();

                            for(String s :strings){

                                months.add(Integer.parseInt(s));
                            }

                            String returnString = "";


                            returnString += "{\n" +
                                    "\t\"error_code\": 0,\n" +
                                    "\t\"data\": [";

                            List<ResDatetotal> list = server.GetYearAndMonthsTotal(Integer.parseInt(json.get("year").toString()),months);

                            if (json.get("plat") != null && json.get("plat").toString().length() > 0) {//选择平台
                                String plat = json.get("plat").toString();
                                list = list.stream().filter(model -> model.getPlat_id().equals(plat)).collect(Collectors.toList());

                            } else {

                            }

                            //安平台分组
                            Map<String, List<ResDatetotal>> map = list.stream().collect(Collectors.groupingBy(ResDatetotal::getPlat_name));
                            int i = 1;
                            for (Map.Entry<String, List<ResDatetotal>> entry : map.entrySet()) {
                                double gmv=entry.getValue().stream().mapToDouble(ResDatetotal::getGvm).sum();
                                double tsxs=entry.getValue().stream().mapToDouble(ResDatetotal::getTs).sum();
                                double tsmb=tsxs+100000;
                                double tsxx=0;
                                double tsdc=(tsxs+tsxx)/tsmb*100;
                                double ml=entry.getValue().stream().mapToDouble(ResDatetotal::getMl).sum();
                                double mlmb=ml+100000;
                                long order_count=entry.getValue().stream().mapToLong(ResDatetotal::getOrdercount).sum();
                                long order_countmb=order_count+100000;
                                returnString += "{\n" +
                                        "\t\t\"xh\": \"" + i+ "\",\n" +
                                        "\t\t\"gvm\": \"" + df.format(gmv) + "\",\n" +
                                        "\t\t\"tsmb\": \"" + df.format(tsmb) + "\",\n" +
                                        "\t\t\"tsxs\": \"" + df.format(tsxs) + "\",\n" +
                                        "\t\t\"tsdc\": \"" + df.format(tsdc) + "%\",\n" +
                                        "\t\t\"tsxx\": \"" + df.format(tsxx) + "\",\n" +
                                        "\t\t\"mlmb\": \"" + df.format(mlmb) + "\",\n" +
                                        "\t\t\"ml\": \"" + df.format(ml) + "\",\n" +
                                        "\t\t\"mldc\": \"" + df.format(ml/mlmb*100) + "%\",\n" +
                                        "\t\t\"order_countmb\": \"" + order_countmb+ "\",\n" +
                                        "\t\t\"order_count\": \"" +order_count + "\",\n" +
                                        "\t\t\"orderdc\": \"" + df.format(order_count/(order_countmb)*100) + "\",\n" +
                                        "\t\t\"plat\": \"" + entry.getKey()+ "\",\n" +
                                        "\t\t\"platgmv\": \"" + df.format(gmv) + "\"\n" +
                                        "\t},\n";

                                i++;
                            }

                            returnString = returnString.substring(0, returnString.length() - 2) + "]\n" +
                                    "}";

                            return returnString;



                        }else {
                            //计算年度

                            String returnString = "";


                            returnString += "{\n" +
                                    "\t\"error_code\": 0,\n" +
                                    "\t\"data\": [";

                            List<ResDatetotal> list = server.GetYearTotal(Integer.parseInt(json.get("year").toString()));

                            if (json.get("plat") != null && json.get("plat").toString().length() > 0) {//选择平台
                                String plat = json.get("plat").toString();
                                list = list.stream().filter(model -> model.getPlat_id().equals(plat)).collect(Collectors.toList());

                            } else {

                            }

                            //安平台分组
                            Map<String, List<ResDatetotal>> map = list.stream().collect(Collectors.groupingBy(ResDatetotal::getPlat_name));
                            int i = 1;
                            for (Map.Entry<String, List<ResDatetotal>> entry : map.entrySet()) {
                                double gmv=entry.getValue().stream().mapToDouble(ResDatetotal::getGvm).sum();
                                double tsxs=entry.getValue().stream().mapToDouble(ResDatetotal::getTs).sum();
                                double tsmb=tsxs+100000;
                                double tsxx=0;
                                double tsdc=(tsxs+tsxx)/tsmb*100;
                                double ml=entry.getValue().stream().mapToDouble(ResDatetotal::getMl).sum();
                                double mlmb=ml+100000;
                                long order_count=entry.getValue().stream().mapToLong(ResDatetotal::getOrdercount).sum();
                                long order_countmb=order_count+100000;
                                returnString += "{\n" +
                                        "\t\t\"xh\": \"" + i+ "\",\n" +
                                        "\t\t\"gvm\": \"" + df.format(gmv) + "\",\n" +
                                        "\t\t\"tsmb\": \"" + df.format(tsmb) + "\",\n" +
                                        "\t\t\"tsxs\": \"" + df.format(tsxs) + "\",\n" +
                                        "\t\t\"tsdc\": \"" + df.format(tsdc) + "%\",\n" +
                                        "\t\t\"tsxx\": \"" + df.format(tsxx) + "\",\n" +
                                        "\t\t\"mlmb\": \"" + df.format(mlmb) + "\",\n" +
                                        "\t\t\"ml\": \"" + df.format(ml) + "\",\n" +
                                        "\t\t\"mldc\": \"" + df.format(ml/mlmb*100) + "%\",\n" +
                                        "\t\t\"order_countmb\": \"" + order_countmb+ "\",\n" +
                                        "\t\t\"order_count\": \"" +order_count + "\",\n" +
                                        "\t\t\"orderdc\": \"" + df.format(order_count/(order_countmb)*100) + "\",\n" +
                                        "\t\t\"plat\": \"" + entry.getKey()+ "\",\n" +
                                        "\t\t\"platgmv\": \"" + df.format(gmv) + "\"\n" +
                                        "\t},\n";

                                i++;
                            }

                            returnString = returnString.substring(0, returnString.length() - 2) + "]\n" +
                                    "}";

                            return returnString;

                        }
                    }
                }
                else{
                    return ReturnUtils.get2004();
                }

            }
            else{
                return ReturnUtils.get2004();
            }
        }
        catch (Exception e){
            return ReturnUtils.get2004();

        }


    }

    /**
     *b2c下钻页
     */
    @PostMapping("/gettotalb2cdetails")
    public String GetTotalB2CDetails(@RequestBody Map params){

        try{

            JSONObject json=new JSONObject(params);
            json=JSON.parseObject(json.toString());
            if (json != null && json.get("userinfo")!=null) {
                if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                    return ReturnUtils.get2002();
                }
            }
            else {
                return ReturnUtils.get2002();
            }
            //定义返回的json
            JSONObject returnJson=new JSONObject();
            if(json!=null&&json.get("queryinfo")!=null)
            {
                json=JSON.parseObject(json.get("queryinfo").toString());
                if(json.get("year")!=null &&json.get("year").toString().length()>0 ){
                    if(json.get("quarter")!=null &&json.get("quarter").toString().length()>0 ){
                        //计算季
                        String returnString = "";


                        returnString += "{\n" +
                                "\t\"error_code\": 0,\n" +
                                "\t\"data\": [";

                        List<ResTotalbyb2Cdate> list = b2cServer.GetYearAndQuarterTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()));
                        int lastyear=Integer.parseInt(json.get("year").toString());
                        int lastquarter=0;
                        if(Integer.parseInt(json.get("quarter").toString())==1)
                        {
                            lastyear=lastyear-1;
                            lastquarter=4;
                        }
                        else {
                            lastquarter=lastquarter-1;
                        }

                        List<ResTotalbyb2Cdate> listhb = b2cServer.GetYearAndQuarterTotal(lastyear,lastquarter);
                        List<ResTotalbyb2Cdate> listtb = b2cServer.GetYearAndQuarterTotal(Integer.parseInt(json.get("year").toString())-1,Integer.parseInt(json.get("quarter").toString()));
                        double total=list.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                        if (json.get("plat") != null && json.get("plat").toString().length() > 0) {//选择平台
                            String plat = json.get("plat").toString();
                            list = list.stream().filter(model -> model.getB2CId().equals(plat)).collect(Collectors.toList());
                            listhb=listhb.stream().filter(model -> model.getB2CId().equals(plat)).collect(Collectors.toList());
                            listtb=listtb.stream().filter(model -> model.getB2CId().equals(plat)).collect(Collectors.toList());

                        } else {

                        }

                        //安平台分组
                        Map<String, List<ResTotalbyb2Cdate>> map = list.stream().collect(Collectors.groupingBy(ResTotalbyb2Cdate::getB2CName));
                        int i = 1;
                        for (Map.Entry<String, List<ResTotalbyb2Cdate>> entry : map.entrySet()) {
                            String zrz="";
                            String fzr="";
                            String zys="";
                            String b2cname= entry.getKey();
                            double ts=entry.getValue().stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                            double tsmb=ts+100000;
                            double tsdc=(ts)/tsmb*100;
                            long order_count=entry.getValue().stream().mapToLong(ResTotalbyb2Cdate::getOrdercount).sum();
                            double kdj=ts/order_count;
                            double tstb=listtb.stream().filter(model -> model.getB2CName().equals(entry.getKey())).collect(Collectors.toList()).stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                            double tb=(ts-tstb)/tstb*100;
                            double tshb=listhb.stream().filter(model -> model.getB2CName().equals(entry.getKey())).collect(Collectors.toList()).stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                            double hb=(ts-tshb)/tshb*100;
                            double zb=ts/total*100;
                            returnString += "{\n" +
                                    "\t\t\"xh\": \"" + i+ "\",\n" +
                                    "\t\t\"zrz\": \"" + zrz+ "\",\n" +
                                    "\t\t\"fzr\": \"" + fzr+ "\",\n" +
                                    "\t\t\"zys\": \"" + zys+ "\",\n" +
                                    "\t\t\"b2cname\": \"" + b2cname+ "\",\n" +
                                    "\t\t\"ts\": \"" + df.format(ts) + "\",\n" +
                                    "\t\t\"tsmb\": \"" + df.format(tsmb) + "\",\n" +
                                    "\t\t\"tsdc\": \"" + df.format(tsdc) + "%\",\n" +
                                    "\t\t\"order_count\": \"" +order_count + "\",\n" +
                                    "\t\t\"kdj\": \"" + df.format(kdj) + "\",\n" +
                                    "\t\t\"tb\": \"" +  df.format(tb)+ "%\",\n" +
                                    "\t\t\"hb\": \"" + df.format(hb)+ "%\",\n" +
                                    "\t\t\"zb\": \"" + df.format(zb) + "%\"\n" +
                                    "\t},\n";
                            i++;
                        }

                        returnString = returnString.substring(0, returnString.length() - 2) + "]\n" +
                                "}";

                        return returnString;


                    }
                    else{
                        if(json.get("months")!=null &&json.get("months").toString().length()>0 )
                        {

                            //计算月

                            String[] strings=json.get("months").toString().split(",");
                            int yearhb=Integer.parseInt(json.get("year").toString());
                            List<Integer> months=new ArrayList<>();

                            List<Integer> monthshb=new ArrayList<>();

                            for(String s :strings){
                                months.add(Integer.parseInt(s));
                            }
                            if(months.size()==1){
                                if(months.get(0)==1){
                                    monthshb.add(12);
                                    yearhb=yearhb-1;
                                }else{
                                    monthshb.add(months.get(0)-1);
                                }
                            }
                            else {
                                if(months.contains(1)){
                                    yearhb=yearhb-1;
                                    monthshb.add(7);
                                    monthshb.add(8);
                                    monthshb.add(9);
                                    monthshb.add(10);
                                    monthshb.add(11);
                                    monthshb.add(12);
                                }
                                else {
                                    monthshb.add(1);
                                    monthshb.add(2);
                                    monthshb.add(3);
                                    monthshb.add(4);
                                    monthshb.add(5);
                                    monthshb.add(6);
                                }

                            }



                            String returnString = "";


                            returnString += "{\n" +
                                    "\t\"error_code\": 0,\n" +
                                    "\t\"data\": [";

                            List<ResTotalbyb2Cdate> list = b2cServer.GetYearAndMonthsTotal(Integer.parseInt(json.get("year").toString()),months);



                            List<ResTotalbyb2Cdate> listhb = b2cServer.GetYearAndMonthsTotal(yearhb,monthshb);
                            List<ResTotalbyb2Cdate> listtb = b2cServer.GetYearAndMonthsTotal(Integer.parseInt(json.get("year").toString())-1,months);
                            double total=list.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();

                            if (json.get("plat") != null && json.get("plat").toString().length() > 0) {//选择平台
                                String plat = json.get("plat").toString();
                                list = list.stream().filter(model -> model.getB2CId().equals(plat)).collect(Collectors.toList());
                                listhb=listhb.stream().filter(model -> model.getB2CId().equals(plat)).collect(Collectors.toList());
                                listtb=listtb.stream().filter(model -> model.getB2CId().equals(plat)).collect(Collectors.toList());

                            } else {

                            }

                            //安平台分组
                            Map<String, List<ResTotalbyb2Cdate>> map = list.stream().collect(Collectors.groupingBy(ResTotalbyb2Cdate::getB2CName));
                            int i = 1;
                            for (Map.Entry<String, List<ResTotalbyb2Cdate>> entry : map.entrySet()) {
                                String zrz="";
                                String fzr="";
                                String zys="";
                                String b2cname= entry.getKey();
                                double ts=entry.getValue().stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                                double tsmb=ts+100000;
                                double tsdc=(ts)/tsmb*100;
                                long order_count=entry.getValue().stream().mapToLong(ResTotalbyb2Cdate::getOrdercount).sum();
                                double kdj=ts/order_count;
                                double tstb=listtb.stream().filter(model -> model.getB2CName().equals(entry.getKey())).collect(Collectors.toList()).stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                                double tb=(ts-tstb)/tstb*100;
                                double tshb=listhb.stream().filter(model -> model.getB2CName().equals(entry.getKey())).collect(Collectors.toList()).stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                                double hb=(ts-tshb)/tshb*100;
                                double zb=ts/total*100;
                                returnString += "{\n" +
                                        "\t\t\"xh\": \"" + i+ "\",\n" +
                                        "\t\t\"zrz\": \"" + zrz+ "\",\n" +
                                        "\t\t\"fzr\": \"" + fzr+ "\",\n" +
                                        "\t\t\"zys\": \"" + zys+ "\",\n" +
                                        "\t\t\"b2cname\": \"" + b2cname+ "\",\n" +
                                        "\t\t\"ts\": \"" + df.format(ts) + "\",\n" +
                                        "\t\t\"tsmb\": \"" + df.format(tsmb) + "\",\n" +
                                        "\t\t\"tsdc\": \"" + df.format(tsdc) + "%\",\n" +
                                        "\t\t\"order_count\": \"" +order_count + "\",\n" +
                                        "\t\t\"kdj\": \"" + df.format(kdj) + "\",\n" +
                                        "\t\t\"tb\": \"" +  df.format(tb)+ "%\",\n" +
                                        "\t\t\"hb\": \"" + df.format(hb)+ "%\",\n" +
                                        "\t\t\"zb\": \"" + df.format(zb) + "%\"\n" +
                                        "\t},\n";
                                i++;
                            }

                            returnString = returnString.substring(0, returnString.length() - 2) + "]\n" +
                                    "}";

                            return returnString;



                        }else {
                            //计算年度

                            String returnString = "";


                            returnString += "{\n" +
                                    "\t\"error_code\": 0,\n" +
                                    "\t\"data\": [";

                            List<ResTotalbyb2Cdate> list = b2cServer.GetYearTotal(Integer.parseInt(json.get("year").toString()));
                            double total=list.stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();


                            if (json.get("plat") != null && json.get("plat").toString().length() > 0) {//选择平台
                                String plat = json.get("plat").toString();
                                list = list.stream().filter(model -> model.getB2CId().equals(plat)).collect(Collectors.toList());
                            } else {

                            }

                            //安平台分组
                            Map<String, List<ResTotalbyb2Cdate>> map = list.stream().collect(Collectors.groupingBy(ResTotalbyb2Cdate::getB2CName));
                            int i = 1;
                            for (Map.Entry<String, List<ResTotalbyb2Cdate>> entry : map.entrySet()) {
                                String zrz="";
                                String fzr="";
                                String zys="";
                                String b2cname= entry.getKey();
                                double ts=entry.getValue().stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                                double tsmb=ts+100000;
                                double tsdc=(ts)/tsmb*100;
                                long order_count=entry.getValue().stream().mapToLong(ResTotalbyb2Cdate::getOrdercount).sum();
                                double kdj=ts/order_count;
                                String tb="-";
                                double tshb=b2cServer.GetYearTotalb2c(Integer.parseInt(json.get("year").toString())-1,entry.getKey()).stream().mapToDouble(ResTotalbyb2Cdate::getTs).sum();
                                double hb=(ts-tshb)/tshb*100;
                                double zb=ts/total*100;
                                returnString += "{\n" +
                                        "\t\t\"xh\": \"" + i+ "\",\n" +
                                        "\t\t\"zrz\": \"" + zrz+ "\",\n" +
                                        "\t\t\"fzr\": \"" + fzr+ "\",\n" +
                                        "\t\t\"zys\": \"" + zys+ "\",\n" +
                                        "\t\t\"b2cname\": \"" + b2cname+ "\",\n" +
                                        "\t\t\"ts\": \"" + df.format(ts) + "\",\n" +
                                        "\t\t\"tsmb\": \"" + df.format(tsmb) + "\",\n" +
                                        "\t\t\"tsdc\": \"" + df.format(tsdc) + "%\",\n" +
                                        "\t\t\"order_count\": \"" +order_count + "\",\n" +
                                        "\t\t\"kdj\": \"" + df.format(kdj) + "\",\n" +
                                        "\t\t\"tb\": \"" + tb+ "%\",\n" +
                                        "\t\t\"hb\": \"" + df.format(hb)+ "%\",\n" +
                                        "\t\t\"zb\": \"" + df.format(zb) + "%\"\n" +
                                        "\t},\n";

                                i++;
                            }

                            returnString = returnString.substring(0, returnString.length() - 2) + "]\n" +
                                    "}";

                            return returnString;

                        }
                    }
                }
                else{
                    return ReturnUtils.get2004();
                }

            }
            else{
                return ReturnUtils.get2004();
            }
        }
        catch (Exception e){
            return ReturnUtils.get2004();

        }


    }



    /**
     *o2o下钻页
     */
    @PostMapping("/gettotalo2odetails")
    public String GetTotalO2ODetails(@RequestBody Map params){
        try{

            JSONObject json=new JSONObject(params);
            json=JSON.parseObject(json.toString());
            if (json != null && json.get("userinfo")!=null) {
                if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                    return ReturnUtils.get2002();
                }
            }
            else {
                return ReturnUtils.get2002();
            }
            //定义返回的json
            JSONObject returnJson=new JSONObject();
            if(json!=null&&json.get("queryinfo")!=null)
            {
                json=JSON.parseObject(json.get("queryinfo").toString());
                if(json.get("year")!=null &&json.get("year").toString().length()>0 ){
                    if(json.get("quarter")!=null &&json.get("quarter").toString().length()>0 ){
                        //计算季
                        String returnString = "";


                        returnString += "{\n" +
                                "\t\"error_code\": 0,\n" +
                                "\t\"data\": [";

                        List<ResTotalbydateareao2O> list = o2oAreaServer.GetYearAndQuarterTotalArea(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()));
                        int lastyear=Integer.parseInt(json.get("year").toString());
                        int lastquarter=0;
                        if(Integer.parseInt(json.get("quarter").toString())==1)
                        {
                            lastyear=lastyear-1;
                            lastquarter=4;
                        }
                        else {
                            lastquarter=lastquarter-1;
                        }

                        List<ResTotalbydateareao2O> listhb = o2oAreaServer.GetYearAndQuarterTotalArea(lastyear,lastquarter);
                        List<ResTotalbydateareao2O> listtb = o2oAreaServer.GetYearAndQuarterTotalArea(Integer.parseInt(json.get("year").toString())-1,Integer.parseInt(json.get("quarter").toString()));


                        if (json.get("city") != null && json.get("city").toString().length() > 0) {//选择平台
                            String city = json.get("city").toString();
                            list = list.stream().filter(model -> model.getCity().equals(city)).collect(Collectors.toList());
                            listhb=listhb.stream().filter(model -> model.getCity().equals(city)).collect(Collectors.toList());
                            listtb=listtb.stream().filter(model -> model.getCity().equals(city)).collect(Collectors.toList());

                        } else {

                        }

                        //安平台分组
                        Map<String, List<ResTotalbydateareao2O>> map = list.stream().collect(Collectors.groupingBy(ResTotalbydateareao2O::getCity));

                        int i = 1;
                        for (Map.Entry<String, List<ResTotalbydateareao2O>> entry : map.entrySet()) {
                            double total=entry.getValue().stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                            Map<String, List<ResTotalbydateareao2O>> mapplat = entry.getValue().stream().collect(Collectors.groupingBy(ResTotalbydateareao2O::getO2OName));
                            for (Map.Entry<String, List<ResTotalbydateareao2O>> entryplat : mapplat.entrySet()) {
                                double ts=entryplat.getValue().stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                                double tsmb=ts+100000;
                                double tsdc=(ts)/tsmb*100;
                                long order_count=entryplat.getValue().stream().mapToLong(ResTotalbydateareao2O::getOrdercount).sum();
                                double kdj=ts/order_count;
                                double tstb=listtb.stream().filter(model -> model.getCity().equals(entry.getKey()) && model.getO2OName().equals(entryplat.getKey())).collect(Collectors.toList()).stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                                double tb=(ts-tstb)/tstb*100;
                                double tshb=listhb.stream().filter(model -> model.getCity().equals(entry.getKey()) && model.getO2OName().equals(entryplat.getKey())).collect(Collectors.toList()).stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                                double hb=(ts-tshb)/tshb*100;
                                double zb=ts/total*100;
                                returnString += "{\n" +
                                        "\t\t\"xh\": \"" + i+ "\",\n" +
                                        "\t\t\"city\": \"" + entry.getKey()+ "\",\n" +
                                        "\t\t\"plat\": \"" + entryplat.getKey()+ "\",\n" +
                                        "\t\t\"ts\": \"" + ts+ "\",\n" +
                                        "\t\t\"tsmb\": \"" + tsmb+ "\",\n" +
                                        "\t\t\"tsdc\": \"" + df.format(tsdc) + "%\",\n" +
                                        "\t\t\"order_count\": \"" + order_count + "\",\n" +
                                        "\t\t\"kdj\": \"" + df.format(kdj) + "\",\n" +
                                        "\t\t\"tb\": \"" +  df.format(tb)+ "%\",\n" +
                                        "\t\t\"hb\": \"" + df.format(hb)+ "%\",\n" +
                                        "\t\t\"zb\": \"" + df.format(zb) + "%\"\n" +
                                        "\t},\n";
                                i++;
                            }


                        }

                        returnString = returnString.substring(0, returnString.length() - 2) + "]\n" +
                                "}";

                        return returnString;


                    }
                    else{
                        if(json.get("months")!=null &&json.get("months").toString().length()>0 )
                        {
                            //计算月



                            String[] strings=json.get("months").toString().split(",");
                            int yearhb=Integer.parseInt(json.get("year").toString());
                            List<Integer> months=new ArrayList<>();

                            List<Integer> monthshb=new ArrayList<>();

                            for(String s :strings){
                                months.add(Integer.parseInt(s));
                            }
                            if(months.size()==1){
                                if(months.get(0)==1){
                                    monthshb.add(12);
                                    yearhb=yearhb-1;
                                }else{
                                    monthshb.add(months.get(0)-1);
                                }
                            }
                            else {
                                if(months.contains(1)){
                                    yearhb=yearhb-1;
                                    monthshb.add(7);
                                    monthshb.add(8);
                                    monthshb.add(9);
                                    monthshb.add(10);
                                    monthshb.add(11);
                                    monthshb.add(12);
                                }
                                else {
                                    monthshb.add(1);
                                    monthshb.add(2);
                                    monthshb.add(3);
                                    monthshb.add(4);
                                    monthshb.add(5);
                                    monthshb.add(6);
                                }

                            }


                            String returnString = "";


                            returnString += "{\n" +
                                    "\t\"error_code\": 0,\n" +
                                    "\t\"data\": [";

                            List<ResTotalbydateareao2O> list = o2oAreaServer.GetYearAndMonthsTotal(Integer.parseInt(json.get("year").toString()),months);


                            List<ResTotalbydateareao2O> listhb = o2oAreaServer.GetYearAndMonthsTotal(yearhb,monthshb);
                            List<ResTotalbydateareao2O> listtb = o2oAreaServer.GetYearAndMonthsTotal(Integer.parseInt(json.get("year").toString())-1,months);



                            if (json.get("city") != null && json.get("city").toString().length() > 0) {//选择平台
                                String city = json.get("city").toString();
                                list = list.stream().filter(model -> model.getCity().equals(city)).collect(Collectors.toList());
                                listhb=listhb.stream().filter(model -> model.getCity().equals(city)).collect(Collectors.toList());
                                listtb=listtb.stream().filter(model -> model.getCity().equals(city)).collect(Collectors.toList());

                            } else {

                            }

                            //安平台分组
                            Map<String, List<ResTotalbydateareao2O>> map = list.stream().collect(Collectors.groupingBy(ResTotalbydateareao2O::getCity));

                            int i = 1;
                            for (Map.Entry<String, List<ResTotalbydateareao2O>> entry : map.entrySet()) {

                                Map<String, List<ResTotalbydateareao2O>> mapplat = entry.getValue().stream().collect(Collectors.groupingBy(ResTotalbydateareao2O::getO2OName));
                                double total=entry.getValue().stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                                for (Map.Entry<String, List<ResTotalbydateareao2O>> entryplat : mapplat.entrySet()) {
                                    double ts=entryplat.getValue().stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                                    double tsmb=ts+100000;
                                    double tsdc=(ts)/tsmb*100;
                                    long order_count=entryplat.getValue().stream().mapToLong(ResTotalbydateareao2O::getOrdercount).sum();
                                    double kdj=ts/order_count;
                                    double tstb=listtb.stream().filter(model -> model.getCity().equals(entry.getKey()) && model.getO2OName().equals(entryplat.getKey())).collect(Collectors.toList()).stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                                    double tb=(ts-tstb)/tstb*100;
                                    double tshb=listhb.stream().filter(model -> model.getCity().equals(entry.getKey()) && model.getO2OName().equals(entryplat.getKey())).collect(Collectors.toList()).stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                                    double hb=(ts-tshb)/tshb*100;
                                    double zb=ts/total*100;
                                    returnString += "{\n" +
                                            "\t\t\"xh\": \"" + i+ "\",\n" +
                                            "\t\t\"city\": \"" + entry.getKey()+ "\",\n" +
                                            "\t\t\"plat\": \"" + entryplat.getKey()+ "\",\n" +
                                            "\t\t\"ts\": \"" + ts+ "\",\n" +
                                            "\t\t\"tsmb\": \"" + tsmb+ "\",\n" +
                                            "\t\t\"tsdc\": \"" + df.format(tsdc) + "%\",\n" +
                                            "\t\t\"order_count\": \"" + order_count + "\",\n" +
                                            "\t\t\"kdj\": \"" + df.format(kdj) + "\",\n" +
                                            "\t\t\"tb\": \"" +  df.format(tb)+ "%\",\n" +
                                            "\t\t\"hb\": \"" + df.format(hb)+ "%\",\n" +
                                            "\t\t\"zb\": \"" + df.format(zb) + "%\"\n" +
                                            "\t},\n";
                                    i++;
                                }


                            }

                            returnString = returnString.substring(0, returnString.length() - 2) + "]\n" +
                                    "}";

                            return returnString;


                        }else {
                            //计算年度


                            String returnString = "";


                            returnString += "{\n" +
                                    "\t\"error_code\": 0,\n" +
                                    "\t\"data\": [";

                            List<ResTotalbydateareao2O> list = o2oAreaServer.GetYearTotalArea(Integer.parseInt(json.get("year").toString()));

                            List<ResTotalbydateareao2O> listhb = o2oAreaServer.GetYearTotalArea(Integer.parseInt(json.get("year").toString()));

                            if (json.get("city") != null && json.get("city").toString().length() > 0) {//选择平台
                                String city = json.get("city").toString();
                                list = list.stream().filter(model -> model.getCity().equals(city)).collect(Collectors.toList());
                            } else {

                            }



                            //安平台分组
                            //安平台分组
                            Map<String, List<ResTotalbydateareao2O>> map = list.stream().collect(Collectors.groupingBy(ResTotalbydateareao2O::getCity));

                            int i = 1;
                            for (Map.Entry<String, List<ResTotalbydateareao2O>> entry : map.entrySet()) {
                                double total=entry.getValue().stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                                Map<String, List<ResTotalbydateareao2O>> mapplat = entry.getValue().stream().collect(Collectors.groupingBy(ResTotalbydateareao2O::getO2OName));
                                for (Map.Entry<String, List<ResTotalbydateareao2O>> entryplat : mapplat.entrySet()) {
                                    double ts=entryplat.getValue().stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                                    double tsmb=ts+100000;
                                    double tsdc=(ts)/tsmb*100;
                                    long order_count=entryplat.getValue().stream().mapToLong(ResTotalbydateareao2O::getOrdercount).sum();
                                    double kdj=ts/order_count;


                                    double tshb=listhb.stream().filter(model -> model.getCity().equals(entry.getKey()) && model.getO2OName().equals(entryplat.getKey())).collect(Collectors.toList()).stream().mapToDouble(ResTotalbydateareao2O::getTs).sum();
                                    double hb=(ts-tshb)/tshb*100;
                                    double zb=ts/total*100;
                                    returnString += "{\n" +
                                            "\t\t\"xh\": \"" + i+ "\",\n" +
                                            "\t\t\"city\": \"" + entry.getKey()+ "\",\n" +
                                            "\t\t\"plat\": \"" + entryplat.getKey()+ "\",\n" +
                                            "\t\t\"ts\": \"" + ts+ "\",\n" +
                                            "\t\t\"tsmb\": \"" + tsmb+ "\",\n" +
                                            "\t\t\"tsdc\": \"" + df.format(tsdc) + "%\",\n" +
                                            "\t\t\"order_count\": \"" + order_count + "\",\n" +
                                            "\t\t\"kdj\": \"" + df.format(kdj) + "\",\n" +
                                            "\t\t\"tb\": \"-\",\n" +
                                            "\t\t\"hb\": \"" + df.format(hb)+ "%\",\n" +
                                            "\t\t\"zb\": \"" + df.format(zb) + "%\"\n" +
                                            "\t},\n";
                                    i++;
                                }


                            }

                            returnString = returnString.substring(0, returnString.length() - 2) + "]\n" +
                                    "}";

                            return returnString;

                        }
                    }
                }
                else{
                    return ReturnUtils.get2004();
                }

            }
            else{
                return ReturnUtils.get2004();
            }
        }
        catch (Exception e){
            return ReturnUtils.get2004();

        }


    }





    private static String fetchGroupKey(User user){
        return user.getName() +"#"+ user.getAddress();
    }
}
