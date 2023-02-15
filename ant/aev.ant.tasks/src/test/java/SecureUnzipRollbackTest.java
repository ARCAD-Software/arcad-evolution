import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl.ArcadCopyTask;
import com.arcadsoftware.mmk.anttasks.taskdefs.rollback.impl.ArcadRollbackTask;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.types.FileSet;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.File;

public class SecureUnzipRollbackTest {



    private static final String ROLLBACK_ID = "111125152500";
    private static final String path = "src/test/resources";

    private static final String fromDirPath = path + "/SecureUnzipRollbackTestContext/tmpafterunzip";
    private static final String toDirPath = path + "/SecureUnzipRollbackTestContext/testziptarget";
    private static final String rollbackDirPath = path + "/SecureUnzipRollbackTestContext/testziprollback";
    private static final String rollbackDirWithIdPath = rollbackDirPath + "/" +ROLLBACK_ID;

    /**
     * From <a href="https://jira.quarcad.net/browse/RDDROPS-1409">RDDROPS-1409</a>
     */
    @Test
    public void testUnzipAndRollback(){

        //Given
        final ArcadCopyTask t = new ArcadCopyTask();
        t.setProject(new Project());
        final File fromDir = new File(fromDirPath);
        final File toDir = new File(toDirPath);
        final File rollbackDir = new File(rollbackDirPath);
        final FileSet set = new FileSet();
        set.setDir(fromDir);
        t.setTodir(toDir);
        t.addFileset(set);
        t.setOverwrite(true);
        t.setRollbackDir(rollbackDir.getAbsolutePath());
        t.setRollbackId(ROLLBACK_ID);
        t.execute();
        File[] afterCopyFileList = toDir.listFiles();
        Assertions.assertNotNull(afterCopyFileList);
        Assertions.assertEquals(2, afterCopyFileList.length);

        final ArcadRollbackTask rollback = new ArcadRollbackTask();
        rollback.setRollbackDir(rollbackDir.getAbsolutePath());
        rollback.setRollbackId(ROLLBACK_ID);

        //When
        rollback.execute();

        //Then
        //Target directory contains only one file (others have been rollbacked)
        afterCopyFileList = toDir.listFiles();
        Assertions.assertNotNull(afterCopyFileList);
        Assertions.assertEquals(1, afterCopyFileList.length);
    }

    @AfterAll
    @BeforeAll
    public static void tearDown(){
        //Clean directories
        try {
            File rollbackDirWithId = new File(rollbackDirWithIdPath + "/rollback-settings.xml");
            rollbackDirWithId.delete();
            rollbackDirWithId = new File(rollbackDirWithIdPath + "/copy1");
            rollbackDirWithId.delete();
            rollbackDirWithId = new File(rollbackDirWithIdPath);
            rollbackDirWithId.delete();
            File unzippedFile = new File(toDirPath + "/test/testFile.txt");
            unzippedFile.delete();
            unzippedFile = new File(toDirPath + "/test");
            unzippedFile.delete();
        }
        catch(Exception e){
            System.out.println("Fail do delete the generated artifacts : " + e.getMessage());
        }
    }
}
