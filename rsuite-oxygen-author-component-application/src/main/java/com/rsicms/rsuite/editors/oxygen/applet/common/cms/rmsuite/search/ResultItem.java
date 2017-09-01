package com.rsicms.rsuite.editors.oxygen.applet.common.cms.rmsuite.search;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang.StringUtils;

import com.rsicms.rsuite.editors.oxygen.applet.common.api.ICmsURI;
import com.rsicms.rsuite.editors.oxygen.applet.common.api.lookup.IReposiotryResource;
import com.rsicms.rsuite.editors.oxygen.applet.common.utils.OxygenIOUtils;

public class ResultItem implements IReposiotryResource {

	private String displayName;
	
	private String rsuiteId;
	
	private String localName;
	
	private String kind;
	
	private String dateCreated;
	
	private String dateModified;
	
	private boolean isXml;
	
	private Map<String, String> aliases;
	
	private String parentId;
	
	private ICmsURI cmsURI;
	
	public void setXml(boolean isXml) {
		this.isXml = isXml;
	}

	private static SimpleDateFormat spf = new SimpleDateFormat("yyyy-MMM-dd HH:mm:ss");
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getRsuiteId() {
		return rsuiteId;
	}

	public void setRsuiteId(String resuiteId) {
		this.rsuiteId = resuiteId;
	}

	public String getLocalName() {
		return localName;
	}

	public void setLocalName(String localName) {
		this.localName = localName;
	}

	public String getKind() {
		return kind;
	}

	public void setKind(String kind) {
		this.kind = kind;
	}

	public String getDateCreated() {
		return dateCreated;
	}
	
	public String getFormattedDateCreated() {
		return formatDate(dateCreated);
	}

	public void setDateCreated(String dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getDateModified() {
		return dateModified;
	}
	
	public String getFormattedDateModified() {
		return formatDate(dateModified);
	}

	public void setDateModified(String dateModified) {
		this.dateModified = dateModified;
	}

	
	public Map<String, String> getAliases() {
		return aliases;
	}

	public void setAliases(Map<String, String> aliases) {
		this.aliases = aliases;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj != null && obj instanceof ResultItem){
			return  ((ResultItem)obj).getRsuiteId().equals(rsuiteId);
		}
		return false;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("id: ").append(rsuiteId).append("\n");
		
		sb.append("displayName: ").append(displayName).append("\n");
		sb.append("kind: ").append(kind).append("\n");
		sb.append("localName: ").append(localName).append("\n");
		
		sb.append("dateCreated: ").append(formatDate(dateCreated)).append("\n");
		sb.append("dateModified: ").append(formatDate(dateModified)).append("\n");
		sb.append("aliases: ").append(aliases);
		
		
		return sb.toString();
	}
	
	private String formatDate(String unixEpoch){
		
		if (unixEpoch == null){
			return "";
		}
		
		Float timestamp = Float.parseFloat(unixEpoch) * 1000;
		Date expiry = new Date(timestamp.longValue() );
		return spf.format(expiry);
	}
	
	public void setCmsURI(ICmsURI cmsURI) {
		this.cmsURI = cmsURI;
	}

	@Override
	public String getDisplayText() {
		return displayName;
	}

	@Override
	public boolean hasChilds() {
		return false;
	}

	@Override
	public boolean isContainer() {
		return false;
	}

	@Override
	public boolean isXml() {
		return isXml;
	}

	@Override
	public String getCMSid() {
		return rsuiteId;
	}

	@Override
	public String getCMSlink() {
		String alias = "";
		
		
		if ((aliases == null || aliases.size() == 0) && parentId != null){
			try {
				String address = cmsURI.getHostURI() + "/rsuite/rest/v1/api/rsuite.oxygen.alias.resolver?rsuiteId=" + parentId + "&" + cmsURI.getSessionKeyParam();
				InputStream is = OxygenIOUtils.loadContentFromURL(address);
				XMLInputFactory factory = XMLInputFactory.newFactory();
				XMLStreamReader xmlReader = factory.createXMLStreamReader(is);
				
				aliases = SearchFacetUtil.parseAliases(xmlReader);				
			} catch (Exception e) {
				throw new RuntimeException("Unable to obtain aliases for a sub mo " + rsuiteId, e);
			}
			
		}
		
		if (aliases != null){
			if (aliases.keySet().size() > 0){
				alias = aliases.keySet().iterator().next();
			}
			
			
			for (Entry<String, String> entry : aliases.entrySet()){
				if ("filename".equalsIgnoreCase(entry.getValue())){
					alias = entry.getKey();
					break;
				}
			}
		}
		
		if (StringUtils.isEmpty(alias)){
			throw new RuntimeException("Unable to create cms link. Missing alias");
		}
		
		return "/rsuite/rest/v2/content/binary/alias/" +alias;
		
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	@Override
	public String getId() {
		return rsuiteId;
	}

	@Override
	public String getCustomMetadata(String metadataName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getCustomMetadataNames() {
		// TODO Auto-generated method stub
		return new HashSet<String>();
	}

	@Override
	public String getIconName() {
		return null;
	}

}
