package com.arcadsoftware.mmk.anttasks.taskdefs.test;



import java.io.File;

import org.apache.tools.ant.types.FileSet;

import com.arcadsoftware.mmk.anttasks.taskdefs.misc.ArcadReplaceTokenValueTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl.ArcadCopyTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl.ArcadRollbackTask;



public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		ArcadCopyTask t = new ArcadCopyTask();
		//t.setCopyWithLog(false);
		//t.setHost("arcad05a");
		//t.setUser("fportier");
		//t.setPwd("ZnBvcnRpZXI=");
		File fromFile = new File("C:\\temp\\arc\\ant-settings.xml");
		File toFile = new File("C:\\temp\\dist\\copy\\arcad_list_remote.xml");
		File rollbackDir = new File("C:\\temp\\backup");		
		FileSet set = new FileSet();
		set.setFile(fromFile);
		t.setTofile(toFile);
		t.setOverwrite(true);
		t.setRollbackDir(rollbackDir.getAbsolutePath());
		t.setRollbackId("111125152500");
		t.setFile(fromFile);
		t.execute();
		
		ArcadRollbackTask rollback = new ArcadRollbackTask();
		rollback.setRollbackDir(rollbackDir.getAbsolutePath());
		rollback.setRollbackId("111125152500");
		rollback.execute();
		
		/*ArcadReplaceTokenValueTask t = new ArcadReplaceTokenValueTask();
		t.setFilePath("C:/eclipse-3.6-birt/workspace/Parcer_scanner_testing/test.xml");
		t.setOccurs("1");
		t.setToken("echo oldoldold");
		t.execute();*/
		
		//<arcad:copywithlog todir="${deploy.dir}" rollbackDir="${arcad.rollback.dir}" rollbackId="${aea.sec.tid}" overwrite="true" copyWithLog="true" host="${aea.server.as400}" user="${aea.user.as400}" pwd="${aea.pwd.as400}" >
		
		/*ArcadCopyWithLogTask t = new ArcadCopyWithLogTask();
		String fromFile = "C:/test/bdc_cor/Web/jsp/ARTICLE.jsp";
		String deployDir = "c:/test/arcad/20130703";
		String arcadRollbackDir = "C:/eclipse-3.4-birt/workspace_V8/ARCAD Home/rollback";
		String aeaSecTid = "130703133000";
		String host = "arcad07d";
		String user = "fportier";
		String pwd = "ZnBvcnRpZXI=";
		String instance = "TS";
		t.setFile(new File(fromFile));
		t.setCopyWithLog(true);
		t.setOverwrite(true);
		t.setRollbackDir(arcadRollbackDir);
		t.setRollbackId(aeaSecTid);
		t.setTodir(new File(deployDir));
		t.setHost(host);
		t.setUser(user);
		t.setPwd(pwd);
		t.setInstance(instance);
		t.execute();*/
		
		/*ListAdditionTask t = new ListAdditionTask();
		t.setFilename("C:\\Program Files\\ARCAD Solutions_09.02.01\\ARCAD Home\\exec_agent\\scripts\\arcad_list_20130703.xml");
		t.setCheckIfExists(true);
		t.setReplaceIfExists(true);
		t.execute();*/
		
		/*ArcadGetTokenValueTask t2 = new ArcadGetTokenValueTask();
		t2.setFilePath("C:/eclipse-3.6-birt/workspace/Parcer_scanner_testing/test.pcml");
		t2.setOccurs("1");
		t2.setToken("program name=test_new");
		t2.setReturnProperty("xml.value");
		t2.execute();*/
		
//		ListExecuteTask t = new ListExecuteTask();
//		t.setListType("fileList");
//		t.setFilename("c:\\devtmp\\forum\\tmp\\sourceList.xml");
//		t.execute();
		
		
//		ListOfFileDeletionTask t2 = new ListOfFileDeletionTask();
//		t2.setFilename("c:\\devtmp\\forum\\tmp\\sourceList.xml");
//		t2.setDeleteQuery("ext='xml'");
//		t2.execute();

		
//		ListDiffTask t = new ListDiffTask();
//		t.setFilename("c:\\devtmp\\dom4j\\test-base.xml");
//		t.setOperandFileName("c:\\devtmp\\dom4j\\test-cmp.xml");
//		t.setResultFileName("c:\\devtmp\\dom4j\\test-result.xml");
//		DiffCondition d = t.createDiffCondition();
//		d.setId("size");
//		d.setOperator(">");
//		t.execute();		
		
		
		/*ListCompareTask t = new ListCompareTask();
		t.setFilename("c:\\devtmp\\dom4j\\test-base.xml");
		t.setOperandFileName("c:\\devtmp\\dom4j\\test-cmp.xml");		
		t.setAddedFileName("c:\\devtmp\\dom4j\\test-added.xml");
		t.setDeletedFileName("c:\\devtmp\\dom4j\\test-deleted.xml");
		t.setCommonFileName("c:\\devtmp\\dom4j\\test-common.xml");		
		t.set("c:\\devtmp\\dom4j\\test-cmp.xml");
		t.setResultFileName("c:\\devtmp\\dom4j\\test-result.xml");
		t.execute();*/			
		

	}

}
