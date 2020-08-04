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
 * ������Ϣ�Ǽǵ�Json�ַ���������
 * @author ����
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
		
		List<String> errorBillNos = new ArrayList<String>();//��¼����ʧ�ܵĵ��ݺ�
		
		int i=1;
		// TODO �Զ����ɵķ������
		if(vos!=null && vos.length>0)
		{
			for(AggBidreqeVO aggBidreqeVO : vos)
			{
				String vdef10 = aggBidreqeVO.getParentVO().getVdef10();
				if(vdef10!=null && !vdef10.equals("")){
					String vdef10Code = getDefdocCode(vdef10);
					if(vdef10Code.equals("1")){//����BIMѡ�ķ����Բ����ͣ��������ѭ��
						continue;
					}	
				}else{
					continue;
				}
				
				//jsonStr�ַ���
//				String jsonStr = "{ \"data\" : {";
				String jsonStr = "{";
				
				//��ȡ������Ϣ���ı�ͷVO
				BidreqeHVO parentVO = aggBidreqeVO.getParentVO();
				//���ñ�ͷVO��Json��Ҫ�ֶ������ֶ�ֵ��װ��Map
				Map<String, String> jsonHeadMap = getJsonHeadFiledValue(parentVO);
				
				//��ȡ��Լ�滮�ӱ�VO����
				BidreqeRplanVO[] rplanVOs = (BidreqeRplanVO[]) aggBidreqeVO.getChildren(BidreqeRplanVO.class);
//				
//				CircularlyAccessibleValueObject[] childrenVOs = aggBidreqeVO.getChildrenVO();
				
				
				
				//��ȡ��װ����Ҫƴװjson�Ľ��
				List<Map<String, String>> jsonBodyListMap = null;
				if(rplanVOs!=null && rplanVOs.length>0){
					jsonBodyListMap = getJsonBodyFiledValue(rplanVOs);
				}
				
				//��ȡ������map
				List<Map<String, String>> files = findfile(parentVO.getPrimaryKey());
				
				//��ȡ��ͷ������Լ�files��json�ַ���
				String jsonStr2 = createJsonStr(jsonHeadMap,jsonBodyListMap);
				
				jsonStr += jsonStr2;
				
				//����data������ �� ��������סdata������
				jsonStr += "}";
				
//				jsonStr = jsonStr.substring(0,jsonStr.length()-1) +"}";
//				System.out.println(jsonStr);
				
				/**  ׼���������ýӿ�����Ҫ��6������ **/
				String project_id = parentVO.getPk_project();//��Ŀ����
				String project_name = findProjectNameByPK(project_id);//��Ŀ����
				String item_id = parentVO.getPk_bidreqe();//��������
				String item_name = parentVO.getVname();//��������
				item_name = item_name.replaceAll("\\n", "");
				String contract_id = "";//��ͬ����
				String contract_name = "";//��ͬ����
				
				String pk_defodc = parentVO.getVdef8();
				String section_id = "";
				if(pk_defodc!=null && !pk_defodc.equals("")){
					 section_id = getDefdocCode(pk_defodc);//BIM��Ŀ
				}
				
				/**
				 * ����BIM���ͽӿ�
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
					// TODO �Զ����ɵ� catch ��
					e.printStackTrace();
					IWritesendlog log = (IWritesendlog)NCLocator.getInstance().lookup(IWritesendlog.class);
					log.writesendlog("���ݺ�"+parentVO.getVbillcode()+"����ʧ��! ԭ��: "+e.getMessage(), "��BIM", "������Ϣ�Ǽǵ�����");
					ExceptionUtils.wrappBusinessException("���ݺ�"+parentVO.getVbillcode()+"����ʧ��! ԭ��: "+e.getMessage());
				}
			}
		}
		
		if(errorBillNos.size()>0)
		{
			String str = "������Ϣ�Ǽǵ�δ���ͳɹ��ĵ��ݺ�Ϊ��";
			for(String billno : errorBillNos)
			{
				str += billno+", ";
			}
			str = str.substring(0, str.length()-2);
			IWritesendlog log = (IWritesendlog)NCLocator.getInstance().lookup(IWritesendlog.class);
			log.writesendlog(str, "��BIM", "������Ϣ�Ǽǵ�δ�ɹ����ݺ�");
			ExceptionUtils.wrappBusinessException(str+"iΪ"+i);
		}
		
	}
	
	/**
	 * ͨ�������װ�õ�map���ӱ��װ�õ�list<Map<String,String>>����Jsonƴװ
	 * @param jsonHeadMap
	 * @param jsonBodyListMap
	 * @return
	 */
	private String createJsonStr(Map<String, String> jsonHeadMap,List<Map<String, String>> jsonBodyListMap)
	{
		//��ʼ����ͷjson��ʽ
		String jsonStr = "\"head\" : {";
		
		//��forѭ������ͷmao�еļ���ֵƴ�ӽ�json����
		for(Map.Entry<String, String> me : jsonHeadMap.entrySet())
		{
			jsonStr += "\"" + me.getKey()+"\" : " + "\"" + me.getValue() + "\",";
		}
		//�ѱ�ͷ���һ����ֵ�Ժ����","ȥ�����ڼ���"},"������ͷ
		jsonStr = jsonStr.substring(0,jsonStr.length()-1) + "},";
		
		//��ʼ������json��ʽ
		jsonStr += "\"body\":{ \""+"pk_bidreqe_rplan"+"\":[";
		
		//��ȡ����List<Map>d�ĳ��ȣ��������壬��ÿһ��map����ļ���ֵ��װ��json
		if(jsonBodyListMap!=null){
			int len = jsonBodyListMap.size();
			for(int i=0; i<len; i++)
			{
				Map<String, String> map = jsonBodyListMap.get(i);
				if(i!=len-1)//�������һ��map�����Ҫ��һ��","
				{
					jsonStr += "{";
					for(Map.Entry<String, String> me : map.entrySet())
					{
						jsonStr += "\"" + me.getKey()+"\" :" +"\"" + me.getValue() + "\",";
					}
					jsonStr = jsonStr.substring(0,jsonStr.length()-1) + "},";
				}
				else{//�����һ��map���Ծ���"]"����pk_bidreqe_rplan����
					jsonStr += "{";
					for(Map.Entry<String, String> me : map.entrySet())
					{
						jsonStr += "\"" + me.getKey()+"\" :" +"\"" + me.getValue() + "\",";
					}
					jsonStr = jsonStr.substring(0,jsonStr.length()-1) + "}";
				}
				
			}
		}
		
		//����body����ʼ��files����   //, \"files\" : [  ��Ҫ�ˣ���
		jsonStr += "]} ";
		
		//����files������װjson
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
	 * ���ݵ���������ѯ�������и���������base64��ʽ�ַ�������
	 * @param pk ��������
	 * @return ����ļ���Map key��ʾ�ļ��� value��ʾ�ļ���ֵ
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
			ExceptionUtils.wrappBusinessException("��ѯ�����쳣��");
		}
		
		return null;
	}
	
	/**
	 * ��parentVOͷVO ȡ����Ӧ���ֶΣ�Ȼ�����Ҫ��valueֵ��ѯ����
	 * @param parentVO
	 * @return Map<String,String> 
	 */
	private Map<String,String> getJsonHeadFiledValue(BidreqeHVO parentVO)
	{
		Map<String,String> map = new LinkedHashMap<String,String>();
		//��ȡ����˾����
		String pk_req_org = parentVO.getPk_req_org();
		//���ݹ�˾������ѯ��˾����
		String orgName = findOrgNameByPK(pk_req_org);
		if(orgName!=null && !orgName.equals("")){
			map.put("pk_req_org", orgName);
		}
		
		//��ȡ��Ŀ
		String pk_project = parentVO.getPk_project();
		map.put("pk_project", pk_project);
		
		//�Ƿ�ִ���б�
		String vdef1 = parentVO.getVdef1();
		if(vdef1.equals("Y")){
			map.put("vdef1", "��");
		}else{
			map.put("vdef1", "��");
		}
		
		//��������
		Date date = parentVO.getDbilldate().toDate();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		String dbilldate = sdf.format(date);
		map.put("dbilldate", dbilldate);
		
		//�Ƶ���������
		String pk_dept = parentVO.getPk_dept();
		//���ݲ���������ѯ��������
		if(pk_dept!=null && !pk_dept.equals("")){
			String deptName = findDeptNameByPK(pk_dept);
			map.put("pk_dept", deptName);
		}else{
			map.put("pk_dept", "");
		}
		
		//����״̬
//		String fstatusflag = parentVO.getFstatusflag()+"";
		map.put("fstatusflag", "�ύ");
		
		//������
		String vbillcode = parentVO.getVbillcode();
		map.put("vbillcode", vbillcode);
		
		//������Ŀ����
		String vname = parentVO.getVname();
		vname = vname.replaceAll("\\n", "");
		map.put("vanme", vname);
		
		//����������
		String pk_bidpsndoc = parentVO.getPk_bidpsndoc();
		if(pk_bidpsndoc!=null && !pk_bidpsndoc.equals("")){
			//����������ѯ����������
			String bidpsndocName = findpsndocNameByPK(pk_bidpsndoc);
			map.put("pk_bidpsndoc", bidpsndocName);
		}else{
			map.put("pk_bidpsndoc", "");
		}
		
		//����Ԥռ���
		String vdef9 = parentVO.getVdef9();
		if(vdef9!=null && !vdef9.equals("")){
			map.put("vdef9", vdef9);
		}else{
			map.put("vdef9", "");
		}
		
		//����˵��
		String vmemo = parentVO.getVmemo();
		if(vmemo!=null && !vmemo.equals("")){
			vmemo = vmemo.replaceAll("\\n", "");
			map.put("vmemo", vmemo);
		}else{
			map.put("vmemo", "");
		}
		
		//������Ϣ�Ǽ�ͷ����
		String pk_bidreqe = parentVO.getPk_bidreqe();
		map.put("pk_bidreqe", pk_bidreqe);
		
		//�Ƶ���
		String billmaker = parentVO.getBillmaker();
		//�����Ƶ���������ѯ�Ƶ�������
		String billmakerName = findUserNameByPK(billmaker);
		map.put("billmaker", billmakerName);
		
		//�Ƶ�����
		Date dmakedate = parentVO.getDmakedate().toDate();
		String dmakedate2 = sdf.format(dmakedate);
		map.put("dmakedate", dmakedate2);
		
		return map;
	}
	
	/**
	 * �����ӱ��VO�����ȡƴװJSON����Ҫ�����м���ֵ��ÿһ��VO��װ��һ��map���ٰ�map��װ��һ��list
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
			
			//��Ŀ���� ��ֱ��������
			String vbdef2 = rplanVO.getVbdef2();
			map.put("vbdef2", vbdef2);
			
			//������� ����
			String vbdef3 = rplanVO.getVbdef3();
			//���ݺ������������ѯ�����������
			String BDCBSName = findBDCBSNameByPK(vbdef3);
			map.put("vbdef3", BDCBSName);
			
			//����Ҫ�� ����
			String vbdef4 = rplanVO.getVbdef4();
			//���ݺ���Ҫ��������ѯ����Ҫ������
			String factorAsoaName = findFactorAsoaNameByPK(vbdef4);
			map.put("vbdef4", factorAsoaName);
			
			//��ֽ��
			String vbdef6 = rplanVO.getVbdef6();
			map.put("vbdef6", vbdef6);
			
			//��ע
			String vmemo = rplanVO.getVmemo();
			if(vmemo!=null && !vmemo.equals("")){
				vmemo = vmemo.replaceAll("\\n", "");
				map.put("vmemo", vmemo);
			}else{
				map.put("vmemo", "");
			}
			
			//��Լ�滮����
			String pk_bidreqe_rplan = rplanVO.getPk_bidreqe_rplan();
			map.put("pk_bidreqe_rplan", pk_bidreqe_rplan);
			
			list.add(map);
		}
		return list;
	}
	
	/**
	 * ���ݺ���Ҫ��������ѯ����Ҫ������
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException("���������Ϣ�쳣��");
		}
		
		return null;
	}
	
	/**
	 * ���ݺ������������ѯ�����������
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
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			ExceptionUtils.wrappBusinessException("���������Ϣ�쳣��");
		}
		
		return null;
	}
	
	/**
	 * ���ݹ�˾������ѯ����˾����
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
			ExceptionUtils.wrappBusinessException("����˾��Ϣ�쳣��");
		}
		return null;
	}
	
	/**
	 * ���ݲ���������ѯ��������
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
			ExceptionUtils.wrappBusinessException("�Ƶ�������Ϣ�쳣��");
		}
		return null;
	}
	
	/**
	 * ������Ա������ѯ����Ա����
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
			ExceptionUtils.wrappBusinessException("��������Ϣ�쳣��");
		}
		return null;
	}
	
	/**
	 * �����û��������û�����
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
			ExceptionUtils.wrappBusinessException("�Ƶ�����Ϣ�쳣��");
		}
		return null;
	}
	
	/**
	 * ͨ����Ŀ������ѯ��Ŀ����
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
			ExceptionUtils.wrappBusinessException("��Ŀ��Ϣ�쳣��");
		}
		return null;
	}
	
	/**
	 * �����Զ��嵵��������ȡ�Զ��嵵������
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
			ExceptionUtils.wrappBusinessException("��ǩ������Ϣ�쳣!!");
		}
		
		return null;
	}
}
