package com.ysyy.biserver.control;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ysyy.biserver.common.ReturnUtils;
import com.ysyy.biserver.common.Validate;
import com.ysyy.biserver.gpmodel.Resmtxzb;
import com.ysyy.biserver.gpmodel.Resymdweek;
import com.ysyy.biserver.gpmodel.Resymdzb;
import com.ysyy.biserver.gpmodel.SLstj;
import com.ysyy.biserver.gpserver.impls.ResmtxzbImpl;
import com.ysyy.biserver.gpserver.impls.ResymdweekImpl;
import com.ysyy.biserver.gpserver.impls.ResymdzbImpl;
import com.ysyy.biserver.gpserver.impls.SLstjImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Date;
import java.text.DecimalFormat;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.Map.Entry.comparingByKey;

@CrossOrigin
@RestController
public class CeoControler {
    @Autowired
    private SLstjImpl lstjServer;
    @Autowired
    private ResymdzbImpl ymdzbServer;
    @Autowired
    private ResymdweekImpl ymdweekServer;
    @Autowired
    private ResmtxzbImpl mtxzbServer;

    private Validate v=new Validate();
    private static final DecimalFormat df = new DecimalFormat("0.00");//保留两位小数点
    private static final DecimalFormat df2 = new DecimalFormat("0");//不保留小数点

    /**
     * 零售统计
     * @param params
     * @return
     */
    @PostMapping("/gettotalls")
    public String GetTotalLs(@RequestBody Map params){
        try{

            JSONObject json=new JSONObject(params);
            json= JSON.parseObject(json.toString());
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


                        int hbquarter=Integer.parseInt(json.get("quarter").toString());
                        int hbyear=Integer.parseInt(json.get("year").toString());
                        if(hbquarter==1){
                            hbquarter=4;
                            hbyear=hbyear-1;
                        }
                        else {

                            hbquarter=hbquarter-1;
                        }
                        List<SLstj> list=lstjServer.GetYearAndQuarterTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()));
                        List<SLstj> listhb=lstjServer.GetYearAndQuarterTotal(hbyear,hbquarter);

                        double total=list.stream().mapToDouble(SLstj::getHsls).sum();//总零售
                        //传统零售
                        List<SLstj> listct=list.stream().filter(model -> model.getSyb().equals("传统")  ).collect(Collectors.toList());
                        List<SLstj> listcthb=listhb.stream().filter(model -> model.getSyb().equals("传统")  ).collect(Collectors.toList());

                        double hslsct=listct.stream().mapToDouble(SLstj::getHsls).sum();
                        double bhslsct=listct.stream().mapToDouble(SLstj::getBhsls).sum();
                        double hsmlct=listct.stream().mapToDouble(SLstj::getHsml).sum();
                        double bhsmlct=listct.stream().mapToDouble(SLstj::getBhsml).sum();
                        double bhsmlcthb=listcthb.stream().mapToDouble(SLstj::getBhsml).sum();

                        //新零售
                        List<SLstj> listxls=list.stream().filter(model -> model.getSyb().equals("新零售")  ).collect(Collectors.toList());
                        List<SLstj> listxlshb=listhb.stream().filter(model -> model.getSyb().equals("新零售")  ).collect(Collectors.toList());

                        double hslsxls=listxls.stream().mapToDouble(SLstj::getHsls).sum();
                        double bhslsxls=listxls.stream().mapToDouble(SLstj::getBhsls).sum();
                        double hsmlxls=listxls.stream().mapToDouble(SLstj::getHsml).sum();
                        double bhsmlxls=listxls.stream().mapToDouble(SLstj::getBhsml).sum();
                        double bhsmlxlshb=listxlshb.stream().mapToDouble(SLstj::getBhsml).sum();
                        String lrhbxls=df2.format((bhsmlxls-bhsmlxlshb)/bhsmlcthb*100).toString();

                        //费用环比
                        double lsfyhbxls=(((hslsxls-bhsmlxls)-(listxlshb.stream().mapToDouble(SLstj::getHsls).sum()-listxlshb.stream().mapToDouble(SLstj::getBhsml).sum()))/(listxlshb.stream().mapToDouble(SLstj::getHsls).sum()-listxlshb.stream().mapToDouble(SLstj::getBhsml).sum())*100);
                        //毛利额环比
                        double hsmlhbxls=(hsmlxls-listxlshb.stream().mapToDouble(SLstj::getHsml).sum())/listxlshb.stream().mapToDouble(SLstj::getHsml).sum()*100;
                        //销售额环比
                        double hslshbxls=(hslsxls-listxlshb.stream().mapToDouble(SLstj::getHsls).sum())/listxlshb.stream().mapToDouble(SLstj::getHsls).sum()*100;


                        return  "{\n" +
                                "    \"error_code\": 0,\n" +
                                "    \"data\": {\n" +
                                "      \"total\": \""+df.format(total)+"\",\n" +
                                "      \"hslsct\": \""+df.format(hslsct)+"\",\n" +
                                "      \"hslsctdc\": \""+df.format(75)+"\",\n" +
                                "      \"hsmlct\": "+df.format(hsmlct)+" ,\n" +
                                "      \"bhsmlct\": \""+df.format(bhsmlct)+"\",\n" +
                                "      \"lsfyct\": \""+df2.format(hslsct-bhsmlct)+"\",\n" +
                                "      \"lrhbct\": \""+df2.format((bhsmlct-bhsmlcthb)/bhsmlcthb*100)+"\",\n" +
                                "      \"hslsxls\": \""+df.format(hslsxls)+"\",\n" +
                                "      \"hslsxlsdc\": \""+df.format(60)+"\",\n" +
                                "      \"hsmlxls\": "+df.format(hsmlxls)+" ,\n" +
                                "      \"bhsmlxls\": \""+df.format(bhsmlxls)+"\",\n" +
                                "      \"lsfyxls\": \""+df2.format(hslsxls-bhsmlxls)+"\",\n" +
                                "      \"lrhbxls\": \""+lrhbxls+"\",\n" +
                                "      \"lsfyhbxls\": \""+lsfyhbxls+"\",\n" +
                                "      \"hsmlhbxls\": \""+hsmlhbxls+"\",\n" +
                                "      \"hslshbxls\": \""+hslshbxls+"\"\n" +
                                "    }\n" +
                                "  }";
                    }
                    else{
                        if(json.get("month")!=null &&json.get("month").toString().length()>0 )
                        {
                            int hbmonth=Integer.parseInt(json.get("month").toString());
                            int hbyear=Integer.parseInt(json.get("year").toString());
                            if(hbmonth==1){
                                hbmonth=12;
                                hbyear=hbyear-1;
                            }
                            else {

                                hbmonth=hbmonth-1;
                            }
                            List<SLstj> list=lstjServer.GetYearAndMonthTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()));
                            List<SLstj> listhb=lstjServer.GetYearAndMonthTotal(hbyear,hbmonth);

                            double total=list.stream().mapToDouble(SLstj::getHsls).sum();//总零售
                            //传统零售
                            List<SLstj> listct=list.stream().filter(model -> model.getSyb().equals("传统")  ).collect(Collectors.toList());
                            List<SLstj> listcthb=listhb.stream().filter(model -> model.getSyb().equals("传统")  ).collect(Collectors.toList());

                            double hslsct=listct.stream().mapToDouble(SLstj::getHsls).sum();
                            double bhslsct=listct.stream().mapToDouble(SLstj::getBhsls).sum();
                            double hsmlct=listct.stream().mapToDouble(SLstj::getHsml).sum();
                            double bhsmlct=listct.stream().mapToDouble(SLstj::getBhsml).sum();
                            double bhsmlcthb=listcthb.stream().mapToDouble(SLstj::getBhsml).sum();

                            //新零售
                            List<SLstj> listxls=list.stream().filter(model -> model.getSyb().equals("新零售")  ).collect(Collectors.toList());
                            List<SLstj> listxlshb=listhb.stream().filter(model -> model.getSyb().equals("新零售")  ).collect(Collectors.toList());

                            double hslsxls=listxls.stream().mapToDouble(SLstj::getHsls).sum();
                            double bhslsxls=listxls.stream().mapToDouble(SLstj::getBhsls).sum();
                            double hsmlxls=listxls.stream().mapToDouble(SLstj::getHsml).sum();
                            double bhsmlxls=listxls.stream().mapToDouble(SLstj::getBhsml).sum();
                            double bhsmlxlshb=listxlshb.stream().mapToDouble(SLstj::getBhsml).sum();
                            String lrhbxls=df2.format((bhsmlxls-bhsmlxlshb)/bhsmlcthb*100).toString();
                            //费用环比
                            double lsfyhbxls=(((hslsxls-bhsmlxls)-(listxlshb.stream().mapToDouble(SLstj::getHsls).sum()-listxlshb.stream().mapToDouble(SLstj::getBhsml).sum()))/(listxlshb.stream().mapToDouble(SLstj::getHsls).sum()-listxlshb.stream().mapToDouble(SLstj::getBhsml).sum())*100);
                            //毛利额环比
                            double hsmlhbxls=(hsmlxls-listxlshb.stream().mapToDouble(SLstj::getHsml).sum())/listxlshb.stream().mapToDouble(SLstj::getHsml).sum()*100;
                            //销售额环比
                            double hslshbxls=(hslsxls-listxlshb.stream().mapToDouble(SLstj::getHsls).sum())/listxlshb.stream().mapToDouble(SLstj::getHsls).sum()*100;


                            return  "{\n" +
                                    "    \"error_code\": 0,\n" +
                                    "    \"data\": {\n" +
                                    "      \"total\": \""+df.format(total)+"\",\n" +
                                    "      \"hslsct\": \""+df.format(hslsct)+"\",\n" +
                                    "      \"hslsctdc\": \""+df.format(75)+"\",\n" +
                                    "      \"hsmlct\": "+df.format(hsmlct)+" ,\n" +
                                    "      \"bhsmlct\": \""+df.format(bhsmlct)+"\",\n" +
                                    "      \"lsfyct\": \""+df2.format(hslsct-bhsmlct)+"\",\n" +
                                    "      \"lrhbct\": \""+df2.format((bhsmlct-bhsmlcthb)/bhsmlcthb*100)+"\",\n" +
                                    "      \"hslsxls\": \""+df.format(hslsxls)+"\",\n" +
                                    "      \"hslsxlsdc\": \""+df.format(60)+"\",\n" +
                                    "      \"hsmlxls\": "+df.format(hsmlxls)+" ,\n" +
                                    "      \"bhsmlxls\": \""+df.format(bhsmlxls)+"\",\n" +
                                    "      \"lsfyxls\": \""+df2.format(hslsxls-bhsmlxls)+"\",\n" +
                                    "      \"lrhbxls\": \""+lrhbxls+"\",\n" +
                                    "      \"lsfyhbxls\": \""+lsfyhbxls+"\",\n" +
                                    "      \"hsmlhbxls\": \""+hsmlhbxls+"\",\n" +
                                    "      \"hslshbxls\": \""+hslshbxls+"\"\n" +
                                    "    }\n" +
                                    "  }";



                        }else{

                            //计算年度

                            int hbyear=Integer.parseInt(json.get("year").toString());
                            hbyear=hbyear-1;

                            List<SLstj> list=lstjServer.GetYearTotal(Integer.parseInt(json.get("year").toString()));
                            List<SLstj> listhb=lstjServer.GetYearTotal(hbyear);

                            double total=list.stream().mapToDouble(SLstj::getHsls).sum();//总零售
                            //传统零售
                            List<SLstj> listct=list.stream().filter(model -> model.getSyb().equals("传统")  ).collect(Collectors.toList());
                            List<SLstj> listcthb=listhb.stream().filter(model -> model.getSyb().equals("传统")  ).collect(Collectors.toList());

                            double hslsct=listct.stream().mapToDouble(SLstj::getHsls).sum();
                            double bhslsct=listct.stream().mapToDouble(SLstj::getBhsls).sum();
                            double hsmlct=listct.stream().mapToDouble(SLstj::getHsml).sum();

                            double bhsmlct=listct.stream().mapToDouble(SLstj::getBhsml).sum();
                            double bhsmlcthb=listcthb.stream().mapToDouble(SLstj::getBhsml).sum();

                            //新零售
                            List<SLstj> listxls=list.stream().filter(model -> model.getSyb().equals("新零售")  ).collect(Collectors.toList());
                            List<SLstj> listxlshb=listhb.stream().filter(model -> model.getSyb().equals("新零售")  ).collect(Collectors.toList());

                            double hslsxls=listxls.stream().mapToDouble(SLstj::getHsls).sum();
                            double bhslsxls=listxls.stream().mapToDouble(SLstj::getBhsls).sum();
                            double hsmlxls=listxls.stream().mapToDouble(SLstj::getHsml).sum();
                            double bhsmlxls=listxls.stream().mapToDouble(SLstj::getBhsml).sum();
                            double bhsmlxlshb=listxlshb.stream().mapToDouble(SLstj::getBhsml).sum();
                            String lrhbct=hbyear==Integer.parseInt(json.get("year").toString())?"-":df2.format((bhsmlct-bhsmlcthb)/bhsmlcthb*100).toString();
                            String lrhbxls=hbyear==Integer.parseInt(json.get("year").toString())?"-":df2.format((bhsmlxls-bhsmlxlshb)/bhsmlcthb*100).toString();
                            //费用环比
                            double lsfyhbxls=(((hslsxls-bhsmlxls)-(listxlshb.stream().mapToDouble(SLstj::getHsls).sum()-listxlshb.stream().mapToDouble(SLstj::getBhsml).sum()))/(listxlshb.stream().mapToDouble(SLstj::getHsls).sum()-listxlshb.stream().mapToDouble(SLstj::getBhsml).sum())*100);
                            //毛利额环比
                            double hsmlhbxls=(hsmlxls-listxlshb.stream().mapToDouble(SLstj::getHsml).sum())/listxlshb.stream().mapToDouble(SLstj::getHsml).sum()*100;
                            //销售额环比
                            double hslshbxls=(hslsxls-listxlshb.stream().mapToDouble(SLstj::getHsls).sum())/listxlshb.stream().mapToDouble(SLstj::getHsls).sum()*100;


                            return  "{\n" +
                                    "    \"error_code\": 0,\n" +
                                    "    \"data\": {\n" +
                                    "      \"total\": \""+df.format(total)+"\",\n" +
                                    "      \"hslsct\": \""+df.format(hslsct)+"\",\n" +
                                    "      \"hslsctdc\": \""+df.format(75)+"\",\n" +
                                    "      \"hsmlct\": "+df.format(hsmlct)+" ,\n" +
                                    "      \"bhsmlct\": \""+df.format(bhsmlct)+"\",\n" +
                                    "      \"lsfyct\": \""+df2.format(hslsct-bhsmlct)+"\",\n" +
                                    "      \"lrhbct\": \""+lrhbct+"\",\n" +
                                    "      \"hslsxls\": \""+df.format(hslsxls)+"\",\n" +
                                    "      \"hslsxlsdc\": \""+df.format(60)+"\",\n" +
                                    "      \"hsmlxls\": "+df.format(hsmlxls)+" ,\n" +
                                    "      \"bhsmlxls\": \""+df.format(bhsmlxls)+"\",\n" +
                                    "      \"lsfyxls\": \""+df2.format(hslsxls-bhsmlxls)+"\",\n" +
                                    "      \"lrhbxls\": \""+lrhbxls+"\",\n" +
                                    "      \"lsfyhbxls\": \""+lsfyhbxls+"\",\n" +
                                    "      \"hsmlhbxls\": \""+hsmlhbxls+"\",\n" +
                                    "      \"hslshbxls\": \""+hslshbxls+"\"\n" +
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
     * 满天星业绩指标
     */
    @PostMapping("/getmtxzb")
    public String GetMtxZB(@RequestBody Map params){


        try{

            JSONObject json=new JSONObject(params);
            json= JSON.parseObject(json.toString());
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
                        List<Resmtxzb> list=mtxzbServer.GetYearAndQuarterTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()));

                        double hsxsmb=list.stream().mapToDouble(Resmtxzb::getHsxsmb).sum();//含税销售目标
                        double hsgmv=list.stream().mapToDouble(Resmtxzb::getHsgmv).sum();//含税实际销售额
                        double hsmlmb=list.stream().mapToDouble(Resmtxzb::getHsmlmb).sum();//含税毛利目标
                        double hsml=list.stream().mapToDouble(Resmtxzb::getHsml).sum();//含税毛利
                        //环比
                        int yearhb=Integer.parseInt(json.get("year").toString());
                        int quarterhb=Integer.parseInt(json.get("quarter").toString());
                        if(quarterhb==1){
                            yearhb=yearhb-1;
                            quarterhb=4;
                        }
                        else{
                            quarterhb=quarterhb-1;
                        }

                        List<Resmtxzb> listhb=mtxzbServer.GetYearAndQuarterTotal(yearhb,quarterhb);
                        double hsxsmbhb=listhb.stream().mapToDouble(Resmtxzb::getHsxsmb).sum();//含税销售目标
                        double hsgmvhb=listhb.stream().mapToDouble(Resmtxzb::getHsgmv).sum();//含税实际销售额
                        double hsmlmbhb=listhb.stream().mapToDouble(Resmtxzb::getHsmlmb).sum();//含税毛利目标
                        double hsmlhb=listhb.stream().mapToDouble(Resmtxzb::getHsml).sum();//含税毛利
                        //环比值
                        double hsxsmb_hb=0;
                        double hsgmv_hb=0;
                        double hsmlmb_hb=0;
                        double hsml_hb=0;

                        if(hsxsmbhb!=0){
                            hsxsmb_hb=(hsxsmb-hsxsmbhb)/hsxsmbhb*100;
                        }
                        if(hsgmvhb!=0){
                            hsgmv_hb=(hsgmv-hsgmvhb)/hsgmvhb*100;
                        }
                        if(hsmlmbhb!=0){
                            hsmlmb_hb=(hsmlmb-hsmlmbhb)/hsmlmbhb*100;
                        }
                        if(hsmlhb!=0){
                            hsml_hb=(hsml-hsmlhb)/hsmlhb*100;
                        }




                        return  "{\n" +
                                "    \"error_code\": 0,\n" +
                                "    \"data\": {\n" +
                                "      \"hsxsmb\": \""+df.format(hsxsmb)+"\",\n" +
                                "      \"hsgmv\": \""+df.format(hsgmv)+"\",\n" +
                                "      \"hsmlmb\": \""+df.format(hsmlmb)+"\",\n" +
                                "      \"hsml\": \""+df.format(hsml)+"\",\n" +
                                "      \"hsxsmb_hb\": "+df.format(hsxsmb_hb)+" ,\n" +
                                "      \"hsgmv_hb\": "+df.format(hsgmv_hb)+" ,\n" +
                                "      \"hsmlmb_hb\": "+df.format(hsmlmb_hb)+" ,\n" +
                                "      \"hsml_hb\": "+df.format(hsml_hb)+" \n" +

                                "    }\n" +
                                "  }";
                    }
                    else{
                        if(json.get("month")!=null &&json.get("month").toString().length()>0 )
                        {
                            List<Resmtxzb> list=mtxzbServer.GetYearAndMonthTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()));

                            double hsxsmb=list.stream().mapToDouble(Resmtxzb::getHsxsmb).sum();//含税销售目标
                            double hsgmv=list.stream().mapToDouble(Resmtxzb::getHsgmv).sum();//含税实际销售额
                            double hsmlmb=list.stream().mapToDouble(Resmtxzb::getHsmlmb).sum();//含税毛利目标
                            double hsml=list.stream().mapToDouble(Resmtxzb::getHsml).sum();//含税毛利
                            //环比
                            int yearhb=Integer.parseInt(json.get("year").toString());
                            int monthhb=Integer.parseInt(json.get("month").toString());
                            if(monthhb==1){
                                yearhb=yearhb-1;
                                monthhb=12;
                            }
                            else{
                                monthhb=monthhb-1;
                            }

                            List<Resmtxzb> listhb=mtxzbServer.GetYearAndMonthTotal(yearhb,monthhb);
                            double hsxsmbhb=listhb.stream().mapToDouble(Resmtxzb::getHsxsmb).sum();//含税销售目标
                            double hsgmvhb=listhb.stream().mapToDouble(Resmtxzb::getHsgmv).sum();//含税实际销售额
                            double hsmlmbhb=listhb.stream().mapToDouble(Resmtxzb::getHsmlmb).sum();//含税毛利目标
                            double hsmlhb=listhb.stream().mapToDouble(Resmtxzb::getHsml).sum();//含税毛利
                            //环比值
                            double hsxsmb_hb=0;
                            double hsgmv_hb=0;
                            double hsmlmb_hb=0;
                            double hsml_hb=0;

                            if(hsxsmbhb!=0){
                                hsxsmb_hb=(hsxsmb-hsxsmbhb)/hsxsmbhb*100;
                            }
                            if(hsgmvhb!=0){
                                hsgmv_hb=(hsgmv-hsgmvhb)/hsgmvhb*100;
                            }
                            if(hsmlmbhb!=0){
                                hsmlmb_hb=(hsmlmb-hsmlmbhb)/hsmlmbhb*100;
                            }
                            if(hsmlhb!=0){
                                hsml_hb=(hsml-hsmlhb)/hsmlhb*100;
                            }




                            return  "{\n" +
                                    "    \"error_code\": 0,\n" +
                                    "    \"data\": {\n" +
                                    "      \"hsxsmb\": \""+df.format(hsxsmb)+"\",\n" +
                                    "      \"hsgmv\": \""+df.format(hsgmv)+"\",\n" +
                                    "      \"hsmlmb\": \""+df.format(hsmlmb)+"\",\n" +
                                    "      \"hsml\": \""+df.format(hsml)+"\",\n" +
                                    "      \"hsxsmb_hb\": "+df.format(hsxsmb_hb)+" ,\n" +
                                    "      \"hsgmv_hb\": "+df.format(hsgmv_hb)+" ,\n" +
                                    "      \"hsmlmb_hb\": "+df.format(hsmlmb_hb)+" ,\n" +
                                    "      \"hsml_hb\": "+df.format(hsml_hb)+" \n" +

                                    "    }\n" +
                                    "  }";




                        }else{

                            //计算年度
                            List<Resmtxzb> list=mtxzbServer.GetYearTotal(Integer.parseInt(json.get("year").toString()));

                            double hsxsmb=list.stream().mapToDouble(Resmtxzb::getHsxsmb).sum();//含税销售目标
                            double hsgmv=list.stream().mapToDouble(Resmtxzb::getHsgmv).sum();//含税实际销售额
                            double hsmlmb=list.stream().mapToDouble(Resmtxzb::getHsmlmb).sum();//含税毛利目标
                            double hsml=list.stream().mapToDouble(Resmtxzb::getHsml).sum();//含税毛利
                            //环比

                            List<Resmtxzb> listhb=mtxzbServer.GetYearTotal(Integer.parseInt(json.get("year").toString())-1);
                            double hsxsmbhb=listhb.stream().mapToDouble(Resmtxzb::getHsxsmb).sum();//含税销售目标
                            double hsgmvhb=listhb.stream().mapToDouble(Resmtxzb::getHsgmv).sum();//含税实际销售额
                            double hsmlmbhb=listhb.stream().mapToDouble(Resmtxzb::getHsmlmb).sum();//含税毛利目标
                            double hsmlhb=listhb.stream().mapToDouble(Resmtxzb::getHsml).sum();//含税毛利
                            //环比值
                            double hsxsmb_hb=0;
                            double hsgmv_hb=0;
                            double hsmlmb_hb=0;
                            double hsml_hb=0;

                            if(hsxsmbhb!=0){
                                hsxsmb_hb=(hsxsmb-hsxsmbhb)/hsxsmbhb*100;
                            }
                            if(hsgmvhb!=0){
                                hsgmv_hb=(hsgmv-hsgmvhb)/hsgmvhb*100;
                            }
                            if(hsmlmbhb!=0){
                                hsmlmb_hb=(hsmlmb-hsmlmbhb)/hsmlmbhb*100;
                            }
                            if(hsmlhb!=0){
                                hsml_hb=(hsml-hsmlhb)/hsmlhb*100;
                            }




                            return  "{\n" +
                                    "    \"error_code\": 0,\n" +
                                    "    \"data\": {\n" +
                                    "      \"hsxsmb\": \""+df.format(hsxsmb)+"\",\n" +
                                    "      \"hsgmv\": \""+df.format(hsgmv)+"\",\n" +
                                    "      \"hsmlmb\": \""+df.format(hsmlmb)+"\",\n" +
                                    "      \"hsml\": \""+df.format(hsml)+"\",\n" +
                                    "      \"hsxsmb_hb\": "+df.format(hsxsmb_hb)+" ,\n" +
                                    "      \"hsgmv_hb\": "+df.format(hsgmv_hb)+" ,\n" +
                                    "      \"hsmlmb_hb\": "+df.format(hsmlmb_hb)+" ,\n" +
                                    "      \"hsml_hb\": "+df.format(hsml_hb)+" \n" +

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
     * 一美多指标
     * @param params
     * @return
     */
    @PostMapping("/getymdzb")
    public String GetYmdzb(@RequestBody Map params){
        try{

            JSONObject json=new JSONObject(params);
            json= JSON.parseObject(json.toString());
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



                        List<Resymdzb> list=ymdzbServer.GetYearAndQuarterTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("quarter").toString()));


                        long shCount=list.stream().mapToLong(Resymdzb::getShcount).sum();//sh
                        long yhCount=list.stream().mapToLong(Resymdzb::getYhcount).sum();//sh
                        long xsCount=list.stream().mapToLong(Resymdzb::getXscount).sum();//sh
                        long xxCount=list.stream().mapToLong(Resymdzb::getXxcount).sum();//sh


                        return  "{\n" +
                                "    \"error_code\": 0,\n" +
                                "    \"data\": {\n" +
                                "      \"shCount\": \""+shCount+"\",\n" +
                                "      \"yhCount\": \""+yhCount+"\",\n" +
                                "      \"xsCount\": \""+xsCount+"\",\n" +
                                "      \"xxCount\": "+xxCount+" \n" +
                                "    }\n" +
                                "  }";
                    }
                    else{
                        if(json.get("month")!=null &&json.get("month").toString().length()>0 )
                        {

                            List<Resymdzb> list=ymdzbServer.GetYearAndMonthTotal(Integer.parseInt(json.get("year").toString()),Integer.parseInt(json.get("month").toString()));
                            long shCount=list.stream().mapToLong(Resymdzb::getShcount).sum();//sh
                            long yhCount=list.stream().mapToLong(Resymdzb::getYhcount).sum();//sh
                            long xsCount=list.stream().mapToLong(Resymdzb::getXscount).sum();//sh
                            long xxCount=list.stream().mapToLong(Resymdzb::getXxcount).sum();//sh


                            return  "{\n" +
                                    "    \"error_code\": 0,\n" +
                                    "    \"data\": {\n" +
                                    "      \"shCount\": \""+shCount+"\",\n" +
                                    "      \"yhCount\": \""+yhCount+"\",\n" +
                                    "      \"xsCount\": \""+xsCount+"\",\n" +
                                    "      \"xxCount\": "+xxCount+" \n" +
                                    "    }\n" +
                                    "  }";



                        }else{

                            //计算年度


                            List<Resymdzb> list=ymdzbServer.GetYearTotal(Integer.parseInt(json.get("year").toString()));
                            long shCount=list.stream().mapToLong(Resymdzb::getShcount).sum();//sh
                            long yhCount=list.stream().mapToLong(Resymdzb::getYhcount).sum();//sh
                            long xsCount=list.stream().mapToLong(Resymdzb::getXscount).sum();//sh
                            long xxCount=list.stream().mapToLong(Resymdzb::getXxcount).sum();//sh


                            return  "{\n" +
                                    "    \"error_code\": 0,\n" +
                                    "    \"data\": {\n" +
                                    "      \"shCount\": \""+shCount+"\",\n" +
                                    "      \"yhCount\": \""+yhCount+"\",\n" +
                                    "      \"xsCount\": \""+xsCount+"\",\n" +
                                    "      \"xxCount\": "+xxCount+" \n" +
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
     * 一美多周数据
     * @param params
     * @return
     */
    @PostMapping("/getymdweek")
    public String GetYmdWeek(@RequestBody Map params){
        try{

            JSONObject json=new JSONObject(params);
            json= JSON.parseObject(json.toString());
            if (json != null && json.get("userinfo")!=null) {
                if (!v.ValidateUser(JSON.parseObject(json.get("userinfo").toString()))) {
                    return ReturnUtils.get2002();
                }
            }
            else {
                return ReturnUtils.get2002();
            }
            //定义返回的json

            if(json!=null&&json.get("queryinfo")!=null)
            {

                List<Resymdweek> list=ymdweekServer.GetList();
                String returnString = "";
                returnString += "{\n" +
                        "\t\"error_code\": 0,\n" +
                        "\t\"data\": {";
                Map<Date, List<Resymdweek>> map = list.stream().collect(Collectors.groupingBy(Resymdweek::getSdate));

                // 按值排序降序
                Map<Date, List<Resymdweek>> sorted = map
                        .entrySet()
                        .stream()
                        .sorted(comparingByKey())//顺序
                        .collect(
                                Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e2,
                                        LinkedHashMap::new));

                String xsh="\t\"xsh\":[";
                String yhcount="\t\"yhcount\":[";
                String shcount="\t\"shcount\":[";

                for (Map.Entry<Date, List<Resymdweek>> entry : sorted.entrySet()) {
                        int xfhy=  entry.getValue().stream().collect(Collectors.groupingBy(Resymdweek::getUserid)).size();
                        int ylsj=  entry.getValue().stream().collect(Collectors.groupingBy(Resymdweek::getMdid)).size();
                        double xse=entry.getValue().stream().mapToDouble(Resymdweek::getSfje).sum();
                        xsh += "{\n" +
                                "\t\t\"sdate\": \"" + entry.getKey().toString().substring(6,entry.getKey().toString().length()) + "\",\n" +
                                "\t\t\"value\": " + xse + "\n" +
                                "\t},\n";

                    yhcount += "{\n" +
                            "\t\t\"sdate\": \"" + entry.getKey().toString().substring(6,entry.getKey().toString().length()) + "\",\n" +
                            "\t\t\"value\": " + xfhy + "\n" +
                            "\t},\n";

                    shcount += "{\n" +
                            "\t\t\"sdate\": \"" + entry.getKey().toString().substring(6,entry.getKey().toString().length()) + "\",\n" +
                            "\t\t\"value\": " + ylsj + "\n" +
                            "\t},\n";

                }
                xsh = xsh.substring(0, xsh.length() - 2) + "]\n" ;
                yhcount = yhcount.substring(0, yhcount.length() - 2) + "]\n" ;
                shcount = shcount.substring(0, shcount.length() - 2) + "]\n" ;

                returnString=returnString+"\n"+xsh+",\n"+yhcount+",\n"+shcount+ "}\n" +
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



}
