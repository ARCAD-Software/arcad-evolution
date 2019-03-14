package com.arcadsoftware.mmk.lists.test;






import java.util.Hashtable;




import com.arcadsoftware.mmk.lists.AbstractXmlList;
import com.arcadsoftware.mmk.lists.IListBrowseListener;
//import com.arcadsoftware.mmk.lists.impl.fillers.DirectoryScanFiller;

import com.arcadsoftware.mmk.lists.impl.fillers.DirectoryScanFiller;
import com.arcadsoftware.mmk.lists.impl.lists.FileList;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;





public class Test implements IListBrowseListener{
	
	
	private boolean show = false;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Test t = new Test();
		t.exec();
	}

	
	public void browse(AbstractXmlList l ) {
		l.addBrowseListener(this);
		long start = System.currentTimeMillis();
		l.browse();
		long end = System.currentTimeMillis();
		System.out.println("Temps de parcours : " + String.valueOf(end-start));
	}
	

	
	public void fill(FileList l ) {
		long start = System.currentTimeMillis();
		l.setFiller(new DirectoryScanFiller(l,"C:\\devtmp\\dom4j\\mergeDir"));		
		l.populate();
		long end = System.currentTimeMillis();
		System.out.println("Temps de remplissage : " + String.valueOf(end-start));
	}	
	

	public void addItems(FileList l,FileList fromList ) {
		long start = System.currentTimeMillis();
		//StoreItem i = l.toStoreItem(new File("c:\\devtmp\\test001\\addedfile-1.txt"));
		//XmlFileList l2 = new XmlFileList();
		//l2.setXmlFileName("c:\\devtmp\\dom4j\\test-base_toAdd.xml");
		l.addItems(fromList,false,true);
		long end = System.currentTimeMillis();
		System.out.println("Temps de d'ajout : " + String.valueOf(end-start));
	}	
	
	
	
	public void addItems(FileList l ) {
		long start = System.currentTimeMillis();
		//StoreItem i = l.toStoreItem(new File("c:\\devtmp\\test001\\addedfile-1.txt"));
		//XmlFileList l2 = new XmlFileList();
		//l2.setXmlFileName("c:\\devtmp\\dom4j\\test-base_toAdd.xml");
		l.addItems(new DirectoryScanFiller(l,"C:\\devtmp\\dom4j\\mergeDir"),true,true);
		long end = System.currentTimeMillis();
		System.out.println("Temps de d'ajout : " + String.valueOf(end-start));
	}		
	
	public void removeItems(FileList l ) {
		long start = System.currentTimeMillis();
		//StoreItem i = l.toStoreItem(new File("C:\\devtmp\\dom4j\\baseDir\\file001.txt"));
		AbstractXmlList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test-remove_toremove.xml");
		l.removeItems(l2);
		long end = System.currentTimeMillis();
		System.out.println("Temps de suppression : " + String.valueOf(end-start));
	}		
	
	public void updateItems(AbstractXmlList l ) {
		long start = System.currentTimeMillis();
		//StoreItem i = l.toStoreItem(new File("C:\\devtmp\\dom4j\\baseDir\\file001.txt"));
		AbstractXmlList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test-update_toupdate.xml");
		l.updateItems(l2);
		long end = System.currentTimeMillis();
		System.out.println("Temps de suppression : " + String.valueOf(end-start));
	}		
	
	public void extractItems(AbstractXmlList l ) {
		long start = System.currentTimeMillis();
		//StoreItem i = l.toStoreItem(new File("C:\\devtmp\\dom4j\\baseDir\\file001.txt"));
		FileList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test-extract-result.xml");
		l.extractItems("ext='bat'",l2,false,true,true);
		long end = System.currentTimeMillis();
		System.out.println("Temps de suppression : " + String.valueOf(end-start));
	}		
	public void removeDuplicate(AbstractXmlList l ) {
		long start = System.currentTimeMillis();
		l.removeDuplicate("");
		long end = System.currentTimeMillis();
		System.out.println("Temps de dédoublonnage : " + String.valueOf(end-start));
	}	
	
	public void reinitializeValue(AbstractXmlList l ) {
		long start = System.currentTimeMillis();
		l.reinitializeValue("size","03081969");
		long end = System.currentTimeMillis();
		System.out.println("Temps de remise à jour : " + String.valueOf(end-start));
	}	
	public void duplicateList(AbstractXmlList l ) {
		long start = System.currentTimeMillis();
		FileList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test-extract-dup.xml");
		l.duplicate(l2);
		long end = System.currentTimeMillis();
		System.out.println("Temps de remise à jour : " + String.valueOf(end-start));
	}		
	
	public void mergeList(AbstractXmlList l ) {
		long start = System.currentTimeMillis();
		//cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = false | replacement = false : OK
		//cas n1 = Liste resultat = L1           | ctrl existence = false | replacement = false : OK
		//cas n1 = Liste resultat = L2           | ctrl existence = false | replacement = false : OK
		//cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = true  | replacement = false : OK
		//cas n1 = Liste resultat = L1           | ctrl existence = true  | replacement = false : OK
		//cas n1 = Liste resultat = L2           | ctrl existence = true  | replacement = false : OK
		//cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = true  | replacement = true  : OK
		//cas n1 = Liste resultat = L1           | ctrl existence = true  | replacement = true  : OK
		//cas n1 = Liste resultat = L2           | ctrl existence = true  | replacement = true  :

		FileList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test-l2.xml");
		FileList l3 = new FileList();
		l3.setXmlFileName("c:\\devtmp\\dom4j\\test-result.xml");		
		l.merge(l2,l2,true,true);
		long end = System.currentTimeMillis();
		System.out.println("Temps de fusion : " + String.valueOf(end-start));
	}
	public void substractList(AbstractXmlList l ) {
		long start = System.currentTimeMillis();
		//cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = false | replacement = false : OK
		//cas n1 = Liste resultat = L1           | ctrl existence = false | replacement = false : OK
		//cas n1 = Liste resultat = L2           | ctrl existence = false | replacement = false : OK
		//cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = true  | replacement = false : OK
		//cas n1 = Liste resultat = L1           | ctrl existence = true  | replacement = false : 
		//cas n1 = Liste resultat = L2           | ctrl existence = true  | replacement = false : 
		//cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = true  | replacement = true  : OK
		//cas n1 = Liste resultat = L1           | ctrl existence = true  | replacement = true  : OK
		//cas n1 = Liste resultat = L2           | ctrl existence = true  | replacement = true  : OK

		AbstractXmlList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test-l2.xml");
		AbstractXmlList l3 = new FileList();
		l3.setXmlFileName("c:\\devtmp\\dom4j\\test-result.xml");		
		l.substract(l2,l2,true,true);
		long end = System.currentTimeMillis();
		System.out.println("Temps de fusion : " + String.valueOf(end-start));
	}	
	public void intersectList(AbstractXmlList l ) {
		long start = System.currentTimeMillis();
		//cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = false | replacement = false : OK
		//cas n1 = Liste resultat = L1           | ctrl existence = false | replacement = false : 
		//cas n1 = Liste resultat = L2           | ctrl existence = false | replacement = false : OK
		//cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = true  | replacement = false : OK
		//cas n1 = Liste resultat = L1           | ctrl existence = true  | replacement = false : 
		//cas n1 = Liste resultat = L2           | ctrl existence = true  | replacement = false : OK
		//cas n1 = Liste resultat <> de L1 et L2 | ctrl existence = true  | replacement = true  : OK
		//cas n1 = Liste resultat = L1           | ctrl existence = true  | replacement = true  : 
		//cas n1 = Liste resultat = L2           | ctrl existence = true  | replacement = true  : OK

		AbstractXmlList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test-cmp.xml");
		AbstractXmlList l3 = new FileList();
		l3.setXmlFileName("c:\\devtmp\\dom4j\\test-result.xml");
		Hashtable<String,String> map = new Hashtable<String,String>();
		map.put("size","<>");
		
		l.intersect(l2,l3,false,false,map);
		long end = System.currentTimeMillis();
		System.out.println("Temps de fusion : " + String.valueOf(end-start));
	}		
	
	
	public void exec() {
		FileList l1 = new FileList();
		l1.setXmlFileName("c:\\devtmp\\dom4j\\nw-test-02.xml");
		FileList l2 = new FileList();
		l2.setXmlFileName("c:\\devtmp\\dom4j\\test100k.xml");		
		addItems(l1,l2);
		//duplicateList(l1);
//		l1.load(false);
//		
//		System.out.println(l1.getElementCount());
		//intersectList(l1);		
	}

	public void elementBrowsed(StoreItem item) {
		if (show)
			System.out.println(item.getUserValue(3));		
	}
	
}
