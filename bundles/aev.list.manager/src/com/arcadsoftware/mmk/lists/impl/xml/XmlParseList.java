package com.arcadsoftware.mmk.lists.impl.xml;

import static com.arcadsoftware.mmk.lists.EListConstants.LST_TAG_COLUMNDEF;
import static com.arcadsoftware.mmk.lists.EListConstants.LST_TAG_HEADER;
import static com.arcadsoftware.mmk.lists.EListConstants.LST_TAG_METADATAS;
import static com.arcadsoftware.mmk.lists.EListConstants.LST_TAG_ROW;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import com.arcadsoftware.mmk.lists.AbstractArcadList;
import com.arcadsoftware.mmk.lists.IXmlLists;
import com.arcadsoftware.mmk.lists.managers.AbstractLoggedObject;
import com.arcadsoftware.mmk.lists.metadata.ListColumnDef;
import com.arcadsoftware.mmk.lists.metadata.ListMetaDatas;
import com.arcadsoftware.mmk.lists.metadata.StoreItem;

public class XmlParseList extends AbstractLoggedObject {

	SimpleDateFormat sd = new SimpleDateFormat("yyyyMMdd-HHmmssSSS");

	IXmlLists xmlList;

	public XmlParseList(final IXmlLists xmlList) {
		this.xmlList = xmlList;
	}

	private XmlPullParser createParser() throws XmlPullParserException {
		return XmlPullParserFactory.newInstance().newPullParser();
	}

	protected void fireElementBrowsed(final StoreItem item) {

	}

	private Date getDate(final String stringDate) {
		try {
			final Date d = sd.parse(stringDate);
			return d;
		} catch (final ParseException e) {
			return new Date();
		}
	}

	public void parse() {
		final String fileName = xmlList.getXmlFileName();
		// xmlList.getList().initStoreItem();
		final File f = new File(fileName);
		if (f.exists()) {
			try (final FileReader reader = new FileReader(f)) {
				final XmlPullParser parser = createParser();
				parser.setInput(reader);
				int eventType = parser.getEventType();
				do {
					while (true) {
						final String tag = parser.getName();
						if (eventType == XmlPullParser.START_TAG) {
							parseHeaderTag(parser, tag);
							parseMetaTag(parser, tag);
							parseRowTag(parser, tag);
						} else if (eventType == XmlPullParser.END_TAG) {
							break;
						}
						eventType = parser.nextTag();
					}
					eventType = parser.next();
				} while (eventType != XmlPullParser.END_DOCUMENT);
			} catch (final FileNotFoundException e1) {
				logError(AbstractArcadList.MODULE_NAME + "::XmlParseList", e1);
			} catch (final XmlPullParserException e1) {
				logError(AbstractArcadList.MODULE_NAME + "::XmlParseList", e1);
			} catch (final IOException e) {
				logError(AbstractArcadList.MODULE_NAME + "::XmlParseList", e);
			}
		}
	}

	private void parseColTag(final XmlPullParser parser, final String name, final int initialEventType)
			throws XmlPullParserException, IOException {
		int eventType = initialEventType;
		// if (name.equals("col")) {
		// int eventType = parser.nextTag();
		if (!xmlList.getList().isStoreItemInitialized()) {
			xmlList.getList().initStoreItem();
		}
		while (true) {
			final String tag = parser.getName();
			if (eventType == XmlPullParser.START_TAG) {
				// int count = parser.getAttributeCount();
				final String atId = parser.getAttributeValue(0);
				final String atValue = parser.getAttributeValue(1);
				xmlList.getList().getStoreItem().setValue(atId, atValue);
			} else if (eventType == XmlPullParser.END_TAG) {

				if (tag.equals("col")) {
					break;
				}
			}
			eventType = parser.nextTag();
		}
		// }
	}

	private void parseHeaderTag(final XmlPullParser parser, final String name)
			throws XmlPullParserException, IOException {
		if (name.equals(LST_TAG_HEADER.getValue())) {
			// Traitement des attributs
			final int count = parser.getAttributeCount();
			for (int i = 0; i < count; i++) {
				final String atName = parser.getAttributeName(i);
				final String atValue = parser.getAttributeValue(i);
				if (atName.equals("createdThe")) {
					xmlList.getList().getHeader().setCreatedThe(getDate(atValue));
				} else if (atName.equals("createdBy")) {
					xmlList.getList().getHeader().setCreatedBy(atValue);
				} else if (atName.equals("lastModifiedThe")) {
					xmlList.getList().getHeader().setLastModifiedThe(getDate(atValue));
				} else if (atName.equals("lastModifiedBy")) {
					xmlList.getList().getHeader().setLastModifiedBy(atValue);
				}
			}
			int eventType = parser.nextTag();
			while (true) {
				final String tag = parser.getName();
				if (eventType == XmlPullParser.START_TAG) {
					if (tag.equals("description")) {
						xmlList.getList().getHeader().setDescription(parser.nextText());
					} else if (tag.equals("comment")) {
						xmlList.getList().getHeader().setComment(parser.nextText());
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					if (tag.equals(LST_TAG_HEADER.getValue())) {
						break;
					}
				}
				eventType = parser.nextTag();
			}
		}
	}

	public void parseInfoOnly() {
		final String fileName = xmlList.getXmlFileName();
		// xmlList.getList().initStoreItem();
		final File f = new File(fileName);
		if (f.exists()) {
			try (final FileReader reader = new FileReader(f)) {
				final XmlPullParser parser = createParser();
				parser.setInput(reader);
				int eventType = parser.getEventType();
				boolean found = false;
				do {
					while (true) {
						final String tag = parser.getName();
						if (eventType == XmlPullParser.START_TAG) {
							parseHeaderTag(parser, tag);
							final ListMetaDatas md = parseMetaTag(parser, tag);
							if (md != null) {
								xmlList.getList().setMetadatas(md);
								xmlList.getList().initStoreItem();
							}
						} else if (eventType == XmlPullParser.END_TAG) {
							if (tag.equals(LST_TAG_METADATAS.getValue())) {
								found = true;
							}
							break;
						}
						eventType = parser.nextTag();
					}
					eventType = parser.next();
				} while (eventType != XmlPullParser.END_DOCUMENT && !found);
			} catch (final FileNotFoundException e1) {
				logError(AbstractArcadList.MODULE_NAME + "::XmlParseList", e1);
			} catch (final XmlPullParserException e1) {
				logError(AbstractArcadList.MODULE_NAME + "::XmlParseList", e1);
			} catch (final IOException e) {
				logError(AbstractArcadList.MODULE_NAME + "::XmlParseList", e);
			}
		}
	}

	private ListMetaDatas parseMetaTag(final XmlPullParser parser, final String name)
			throws XmlPullParserException, IOException {
		if (name.equals(LST_TAG_METADATAS.getValue())) {
			// xmlList.getList().getMetadatas().clear();
			final ListMetaDatas storedMetadata = new ListMetaDatas();
			storedMetadata.clear();
			int eventType = parser.nextTag();
			while (true) {
				final String tag = parser.getName();
				if (eventType == XmlPullParser.START_TAG) {
					if (tag.equals(LST_TAG_COLUMNDEF.getValue())) {
						final ListColumnDef cd = new ListColumnDef();
						final int count = parser.getAttributeCount();
						for (int i = 0; i < count; i++) {
							final String atName = parser.getAttributeName(i);
							final String atValue = parser.getAttributeValue(i);
							if (atName.equals("id")) {
								cd.setId(atValue);
							} else if (atName.equals("propertyName")) {
								cd.setPropertyName(atValue);
							} else if (atName.equals("datatype")) {
								cd.setDatatypeFromText(atValue);
							} else if (atName.equals("key")) {
								cd.setKey(atValue.equalsIgnoreCase("true"));
							}
						}
						storedMetadata.addColumnDef(cd);
					}
				} else if (eventType == XmlPullParser.END_TAG) {
					if (tag.equals(LST_TAG_METADATAS.getValue())) {
						// TODO [LM] c'est le bon endroit pour valider les
						// métadatas stockées en terme d'Id et de version.
						if (xmlList.getList().getMetadatas().count() == xmlList.getList().getMetadatas()
								.getFixedMetadataCount()) {
							xmlList.getList().setMetadatas(storedMetadata);
						}
						break;
					}
				}
				eventType = parser.nextTag();
			}
			return storedMetadata;
		}
		return null;
	}

	private void parseRowTag(final XmlPullParser parser, final String name) throws XmlPullParserException, IOException {
		if (name.equals(LST_TAG_ROW.getValue())) {
			int eventType = parser.nextTag();
			while (true) {
				final String tag = parser.getName();
				if (eventType == XmlPullParser.START_TAG) {
					parseColTag(parser, tag, eventType);
				} else if (eventType == XmlPullParser.END_TAG) {
					if (tag.equals(LST_TAG_ROW.getValue())) {
						fireElementBrowsed(xmlList.getList().getStoreItem());
						break;
					}
				}
				eventType = parser.nextTag();
			}
		}
	}
}
