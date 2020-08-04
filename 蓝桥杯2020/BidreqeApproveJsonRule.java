package nc.bs.fdcpp.pp0125.bp.rule;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;

import com.itextpdf.text.log.SysoLogger;

import nc.bs.framework.common.NCLocator;
import nc.bs.pub.filesystem.IFileSystemService;
import nc.bs.tjufitf.bill.Plugin.Writesendlog;
import nc.bs.trade.business.HYPubBO;
import nc.impl.pubapp.pattern.data.bill.BillQuery;
import nc.impl.pubapp.pattern.rule.IRule;
import nc.itf.tjufitf.basicitf.base.IDataQuery;
import nc.itf.tjufitf.bill.bim.IPushBillToBIM;
import nc.itf.tjufitf.bill.bim.IWritesendlog;
import nc.uif.pub.exception.UifException;
import nc.vo.fdcpp.pp0125.AggBidreqeVO;
import nc.vo.fdcpp.pp0125.BidreqeHVO;
import nc.vo.fdcpp.pp0125.BidreqeRplanVO;
import nc.vo.fdcpub.pm0008.BDCBSVO;
import nc.vo.org.DeptVO;
import nc.vo.org.OrgVO;
import nc.vo.pmpub.project.ProjectHeadVO;
import nc.vo.pub.CircularlyAccessibleValueObject;
import nc.vo.pub.SuperVO;
import nc.vo.resa.factor.FactorAsoaVO;
import nc.vo.sm.UserVO;

import nc.vo.bd.defdoc.DefdocVO;
import nc.vo.bd.psn.PsndocVO;
import nc.vo.ecpubapp.pattern.exception.ExceptionUtils;
/**
 * 立项信息登记单Json字符串处理类
 * @author 郭凯
 *
 */
public class BidreqeApproveJsonRule implements IRule<AggBidreqeVO> {

	private IDataQuery dataQuery = null;

	public IDataQuery getDataQuery() {
		if (dataQuery == null) {
			dataQuery = NCLocator.getInstance().lookup(IDataQuery.class);
		}
		return dataQuery;
	}
	
	@Override
	public void process(AggBidreqeVO vos[]) {
		
		List<String> errorBillNos = new ArrayList<String>();//记录发送失败的单据号
		
		int i=1;
		// TODO 自动生成的方法存根
		if(vos!=null && vos.length>0)
		{
			for(AggBidreqeVO aggBidreqeVO : vos)
			{
				String vdef10 = aggBidreqeVO.getParentVO().getVdef10();
				if(vdef10!=null && !vdef10.equals("")){
					String vdef10Code = getDefdocCode(vdef10);
					if(vdef10Code.equals("1")){//推送BIM选的否，所以不推送，跳过这次循环
						continue;
					}	
				}else{
					continue;
				}
				
				//jsonStr字符串
//				String jsonStr = "{ \"data\" : {";
				String jsonStr = "{";
				
				//获取立项信息单的表头VO
				BidreqeHVO parentVO = aggBidreqeVO.getParentVO();
				//利用表头VO将Json需要字段名与字段值封装成Map
				Map<String, String> jsonHeadMap = getJsonHeadFiledValue(parentVO);
				
				//获取合约规划子表VO数组
				BidreqeRplanVO[] rplanVOs = (BidreqeRplanVO[]) aggBidreqeVO.getChildren(BidreqeRplanVO.class);
//				
//				CircularlyAccessibleValueObject[] childrenVOs = aggBidreqeVO.getChildrenVO();
				
				
				
				//获取封装后需要拼装json的结果
				List<Map<String, String>> jsonBodyListMap = null;
				if(rplanVOs!=null && rplanVOs.length>0){
					jsonBodyListMap = getJsonBodyFiledValue(rplanVOs);
				}
				
				//获取附件的map
				List<Map<String, String>> files = findfile(parentVO.getPrimaryKey());
				
				//获取表头与表体以及files的json字符串
				String jsonStr2 = createJsonStr(jsonHeadMap,jsonBodyListMap);
				
				jsonStr += jsonStr2;
				
				//结束data的括弧 和 结束包裹住data的括弧
				jsonStr += "}";
				
//				jsonStr = jsonStr.substring(0,jsonStr.length()-1) +"}";
//				System.out.println(jsonStr);
				
				/**  准备出来调用接口所需要的6个参数 **/
				String project_id = parentVO.getPk_project();//项目主键
				String project_name = findProjectNameByPK(project_id);//项目名称
				String item_id = parentVO.getPk_bidreqe();//立项主键
				String item_name = parentVO.getVname();//立项名称
				item_name = item_name.replaceAll("\\n", "");
				String contract_id = "";//合同主键
				String contract_name = "";//合同名称
				
				String pk_defodc = parentVO.getVdef8();
				String section_id = "";
				if(pk_defodc!=null && !pk_defodc.equals("")){
					 section_id = getDefdocCode(pk_defodc);//BIM项目
				}
				
				/**
				 * 调用BIM发送接口
				 */
				IPushBillToBIM push= (IPushBillToBIM)NCLocator.getInstance().lookup(IPushBillToBIM.class);
				try {
					i++;
					boolean flag=push.PushbillToBIM("1", project_id, project_name, item_id, item_name, contract_id, contract_name, jsonStr, files,section_id);
					if(flag == false)
					{
						errorBillNos.add(parentVO.getVbillcode());
					}
				} catch (Exception e) {
					// TODO 自动生成的 catch 块
					e.printStackTrace();
					IWritesendlog log = (IWritesendlog)NCLocator.getInstance().lookup(IWritesendlog.class);
					log.writesendlog("单据号"+parentVO.getVbillcode()+"发送失败! 原因: "+e.getMessage(), "推BIM", "立项信息登记单报错");
					ExceptionUtils.wrappBusinessException("单据号"+parentVO.getVbillcode()+"发送失败! 原因: "+e.getMessage());
				}
			}
		}
		
		if(errorBillNos.size()>0)
		{
			String str = "立项信息登记单未发送成功的单据号为：";
			for(String billno : errorBillNos)
			{
				str += billno+", ";
			}
			str = str.substring(0, str.length()-2);
			IWritesendlog log = (IWritesendlog)NCLocator.getInstance().lookup(IWritesendlog.class);
			log.writesendlog(str, "推BIM", "立项信息登记单未成功单据号");
			ExceptionUtils.wrappBusinessException(str+"i为"+i);
		}
		
	}
	
	/**
	 * 通过主表封装好的map和子表封装好的list<Map<String,String>>进行Json拼装
	 * @param jsonHeadMap
	 * @param jsonBodyListMap
	 * @return
	 */
	private String createJsonStr(Map<String, String> jsonHeadMap,List<Map<String, String>> jsonBodyListMap)
	{
		//初始化表头json格式
		String jsonStr = "\"head\" : {";
		
		//用for循环将表头mao中的键和值拼接进json串中
		for(Map.Entry<String, String> me : jsonHeadMap.entrySet())
		{
			jsonStr += "\"" + me.getKey()+"\" : " + "\"" + me.getValue() + "\",";
		}
		//把表头最后一个键值对后面的","去掉，在加上"},"结束表头
		jsonStr = jsonStr.substring(0,jsonStr.length()-1) + "},";
		
		//初始化表体json格式
		jsonStr += "\"body\":{ \""+"pk_bidreqe_rplan"+"\":[";
		
		//获取表体List<Map>d的长度，遍历表体，将每一个map里面的键和值封装进json
		if(jsonBodyListMap!=null){
			int len = jsonBodyListMap.size();
			for(int i=0; i<len; i++)
			{
				Map<String, String> map = jsonBodyListMap.get(i);
				if(i!=len-1)//不是最后一个map后面就要加一个","
				{
					jsonStr += "{";
					for(Map.Entry<String, String> me : map.entrySet())
					{
						jsonStr += "\"" + me.getKey()+"\" :" +"\"" + me.getValue() + "\",";
					}
					jsonStr = jsonStr.substring(0,jsonStr.length()-1) + "},";
				}
				else{//是最后一个map所以就用"]"结束pk_bidreqe_rplan数组
					jsonStr += "{";
					for(Map.Entry<String, String> me : map.entrySet())
					{
						jsonStr += "\"" + me.getKey()+"\" :" +"\"" + me.getValue() + "\",";
					}
					jsonStr = jsonStr.substring(0,jsonStr.length()-1) + "}";
				}
				
			}
		}
		
		//结束body并初始化files数组   //, \"files\" : [  不要了！！
		jsonStr += "]} ";
		
		//遍历files集合组装json
		/*
		for(int i=0;i<files.size();i++)
		{
			Map<String,String> map = files.get(i);
			if(i!=files.size()-1)
			{
				jsonStr += "{ ";
				for(Map.Entry<String, String> me : map.entrySet())
				{
					jsonStr += "\""+ me.getKey()+"\" :" +"\"" +me.getValue() +"\",";
				}
				jsonStr = jsonStr.substring(0, jsonStr.length()-1) + "},";
			}
			else
			{
				jsonStr += "{ ";
				for(Map.Entry<String, String> me : map.entrySet())
				{
					jsonStr += "\""+ me.getKey()+"\" :" +"\"" +me.getValue() +"\",";
				}
				jsonStr = jsonStr.substring(0, jsonStr.length()-1) + "}";
			}
			
		}
		*/
//		jsonStr += "]";
		
		return jsonStr;
	}
	
	/**
	 * 根据单据主键查询附件所有附件并返回base64格式字符串数组
	 * @param pk 单据主键
	 * @return 存放文件的Map key表示文件名 value表示文件的值
	 */
	private List<Map<String,String>> findfile(String pk) 
	{
		IFileSystemService service = (IFileSystemService) NCLocator.getInstance().lookup(IFileSystemService.class);
		
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		Map<String,String> map = null;
		
		try {
			ArrayList<HashMap> results= getDataQuery().getResult("select filepath from sm_pub_filesystem where filepath like '"+pk+"/%'");
			
			String filetype="";
			String fieldvalue="";
			
			if(results!=null && results.size()>0)
			{
//				int i = 1;
				for(HashMap result : results )
				{
					map = new LinkedHashMap<String,String>();
					for(Object key : result.keySet())
					{
						String filepath=(String) result.get(key);
//						filepath=filepath.replace("/", "\\");
						ByteArrayOutputStream file = new ByteArrayOutputStream();
						service.downLoadFile(filepath, file);
						String[] split= filepath.split("/");
						byte[] data = file.toByteArray();
						String base64str= new String(Base64.encodeBase64(data));
						filetype=split[split.length-1];
						fieldvalue=base64str;
						
						map.put(filetype, fieldvalue); 
//						map.put("file"+i, fieldvalue);

					}
					list.add(map);
					
//					i++;
				}
			}
			
			return list;
			
		} catch (Exception e) {
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException("查询附件异常！");
		}
		
		return null;
	}
	
	/**
	 * 将parentVO头VO 取出对应的字段，然后把需要的value值查询出来
	 * @param parentVO
	 * @return Map<String,String> 
	 */
	private Map<String,String> getJsonHeadFiledValue(BidreqeHVO parentVO)
	{
		Map<String,String> map = new LinkedHashMap<String,String>();
		//获取需求公司主键
		String pk_req_org = parentVO.getPk_req_org();
		//根据公司主键查询公司名称
		String orgName = findOrgNameByPK(pk_req_org);
		if(orgName!=null && !orgName.equals("")){
			map.put("pk_req_org", orgName);
		}
		
		//获取项目
		String pk_project = parentVO.getPk_project();
		map.put("pk_project", pk_project);
		
		//是否执行招标
		String vdef1 = parentVO.getVdef1();
		if(vdef1.equals("Y")){
			map.put("vdef1", "是");
		}else{
			map.put("vdef1", "否");
		}
		
		//单据日期
		Date date = parentVO.getDbilldate().toDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dbilldate = sdf.format(date);
		map.put("dbilldate", dbilldate);
		
		//制单部门主键
		String pk_dept = parentVO.getPk_dept();
		//根据部门主键查询部门名称
		if(pk_dept!=null && !pk_dept.equals("")){
			String deptName = findDeptNameByPK(pk_dept);
			map.put("pk_dept", deptName);
		}else{
			map.put("pk_dept", "");
		}
		
		//单据状态
//		String fstatusflag = parentVO.getFstatusflag()+"";
		map.put("fstatusflag", "提交");
		
		//立项单编号
		String vbillcode = parentVO.getVbillcode();
		map.put("vbillcode", vbillcode);
		
		//立项项目名称
		String vname = parentVO.getVname();
		vname = vname.replaceAll("\\n", "");
		map.put("vanme", vname);
		
		//经办人主键
		String pk_bidpsndoc = parentVO.getPk_bidpsndoc();
		if(pk_bidpsndoc!=null && !pk_bidpsndoc.equals("")){
			//根据主键查询经办人名称
			String bidpsndocName = findpsndocNameByPK(pk_bidpsndoc);
			map.put("pk_bidpsndoc", bidpsndocName);
		}else{
			map.put("pk_bidpsndoc", "");
		}
		
		//立项预占金额
		String vdef9 = parentVO.getVdef9();
		if(vdef9!=null && !vdef9.equals("")){
			map.put("vdef9", vdef9);
		}else{
			map.put("vdef9", "");
		}
		
		//立项说明
		String vmemo = parentVO.getVmemo();
		if(vmemo!=null && !vmemo.equals("")){
			vmemo = vmemo.replaceAll("\\n", "");
			map.put("vmemo", vmemo);
		}else{
			map.put("vmemo", "");
		}
		
		//立项信息登记头主键
		String pk_bidreqe = parentVO.getPk_bidreqe();
		map.put("pk_bidreqe", pk_bidreqe);
		
		//制单人
		String billmaker = parentVO.getBillmaker();
		//根据制单人主键查询制单人名称
		String billmakerName = findUserNameByPK(billmaker);
		map.put("billmaker", billmakerName);
		
		//制单日期
		Date dmakedate = parentVO.getDmakedate().toDate();
		String dmakedate2 = sdf.format(dmakedate);
		map.put("dmakedate", dmakedate2);
		
		return map;
	}
	
	/**
	 * 根据子表的VO数组获取拼装JSON所需要的所有键和值，每一个VO封装成一个map，再把map封装成一个list
	 * @param rplanVOs
	 * @return
	 */
	private List<Map<String,String>> getJsonBodyFiledValue(BidreqeRplanVO[] rplanVOs)
	{
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map<String,String> map = null;
		for(BidreqeRplanVO rplanVO : rplanVOs)
		{
			map = new LinkedHashMap<String,String>();
			
			//项目名称 （直接主键）
			String vbdef2 = rplanVO.getVbdef2();
			map.put("vbdef2", vbdef2);
			
			//核算对象 主键
			String vbdef3 = rplanVO.getVbdef3();
			//根据核算对象主键查询核算对象名称
			String BDCBSName = findBDCBSNameByPK(vbdef3);
			map.put("vbdef3", BDCBSName);
			
			//核算要素 主键
			String vbdef4 = rplanVO.getVbdef4();
			//根据核算要素主键查询核算要素名称
			String factorAsoaName = findFactorAsoaNameByPK(vbdef4);
			map.put("vbdef4", factorAsoaName);
			
			//拆分金额
			String vbdef6 = rplanVO.getVbdef6();
			map.put("vbdef6", vbdef6);
			
			//备注
			String vmemo = rplanVO.getVmemo();
			if(vmemo!=null && !vmemo.equals("")){
				vmemo = vmemo.replaceAll("\\n", "");
				map.put("vmemo", vmemo);
			}else{
				map.put("vmemo", "");
			}
			
			//合约规划主键
			String pk_bidreqe_rplan = rplanVO.getPk_bidreqe_rplan();
			map.put("pk_bidreqe_rplan", pk_bidreqe_rplan);
			
			list.add(map);
		}
		return list;
	}
	
	/**
	 * 根据核算要素主键查询核算要素名称
	 * @param pkFactorAsoa
	 * @return
	 */
	private String findFactorAsoaNameByPK(String pkFactorAsoa)
	{
		HYPubBO hyPubBO = new HYPubBO();
		
		try {
			FactorAsoaVO factorAsoaVO = (FactorAsoaVO) hyPubBO.queryByPrimaryKey(FactorAsoaVO.class, pkFactorAsoa);
			return factorAsoaVO.getFactorname();
		} catch (UifException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException("核算对象信息异常！");
		}
		
		return null;
	}
	
	/**
	 * 根据核算对象主键查询核算对象名称
	 * @param pk_BDCBS
	 * @return
	 */
	private String findBDCBSNameByPK(String pk_BDCBS)
	{
		HYPubBO hyPubBO = new HYPubBO();
		
		try {
			BDCBSVO bdcbsVO = (BDCBSVO) hyPubBO.queryByPrimaryKey(BDCBSVO.class, pk_BDCBS);
			return bdcbsVO.getName();
		} catch (UifException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException("核算对象信息异常！");
		}
		
		return null;
	}
	
	/**
	 * 根据公司主键查询出公司名称
	 * @param pkOrg
	 * @return
	 */
	private String findOrgNameByPK(String pkOrg)
	{
		HYPubBO hyPubBO = new HYPubBO();
		try {
			OrgVO orgVO = (OrgVO) hyPubBO.queryByPrimaryKey(OrgVO.class, pkOrg);
			return orgVO.getName();
		} catch (UifException e) {
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException("需求公司信息异常！");
		}
		return null;
	}
	
	/**
	 * 根据部门主键查询部门名称
	 * @param pkDept
	 * @return
	 */
	private String findDeptNameByPK(String pkDept)
	{
		HYPubBO hyPubBO = new HYPubBO();
		try {
			DeptVO deptVO = (DeptVO) hyPubBO.queryByPrimaryKey(DeptVO.class, pkDept);
			return deptVO.getName();
		} catch (UifException e) {
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException("制单部门信息异常！");
		}
		return null;
	}
	
	/**
	 * 根据人员主键查询出人员名称
	 * @param pkPsndoc
	 * @return
	 */
	private String findpsndocNameByPK(String pkPsndoc)
	{
		HYPubBO hyPubBO = new HYPubBO();
		try {
			PsndocVO psndocVO = (PsndocVO) hyPubBO.queryByPrimaryKey(PsndocVO.class, pkPsndoc);
			return psndocVO.getName();
		} catch (UifException e) {
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException("经办人信息异常！");
		}
		return null;
	}
	
	/**
	 * 根据用户主键查用户名称
	 * @param pkPsndoc
	 * @return
	 */
	private String findUserNameByPK(String pkUser)
	{
		HYPubBO hyPubBO = new HYPubBO();
		try {
			UserVO userVO = (UserVO) hyPubBO.queryByPrimaryKey(UserVO.class, pkUser);
			return userVO.getUser_name();
		} catch (UifException e) {
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException("制单人信息异常！");
		}
		return null;
	}
	
	/**
	 * 通过项目主键查询项目名称
	 * @param pkProject
	 * @return
	 */
	private String findProjectNameByPK(String pkProject)
	{
		HYPubBO hyPubBO = new HYPubBO();
		try {
			ProjectHeadVO headVO = (ProjectHeadVO) hyPubBO.queryByPrimaryKey(ProjectHeadVO.class,pkProject);
			return headVO.getProject_name();
		} catch (UifException e) {
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException("项目信息异常！");
		}
		return null;
	}
	
	/**
	 * 根据自定义档案主键获取自定义档案编码
	 * @param pkDefdoc
	 * @return
	 */
	private String getDefdocCode(String pkDefdoc)
	{
		HYPubBO hyPubBO = new HYPubBO();
		
		try {
			DefdocVO defdocVO = (DefdocVO) hyPubBO.queryByPrimaryKey(DefdocVO.class, pkDefdoc);
			return defdocVO.getCode();
		} catch (UifException e) {
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException("手签类型信息异常!!");
		}
		
		return null;
	}
}
