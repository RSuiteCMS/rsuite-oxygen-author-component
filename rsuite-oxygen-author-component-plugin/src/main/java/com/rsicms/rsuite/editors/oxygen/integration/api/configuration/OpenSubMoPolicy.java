package com.rsicms.rsuite.editors.oxygen.integration.api.configuration;

import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class OpenSubMoPolicy {

	private Set<String> topicSubMos;

	private SubMoPolicy openSubMoPolicy = SubMoPolicy.TOP_MO;

	public static OpenSubMoPolicy parseSubMoPolicyElement(Element element) {

		OpenSubMoPolicy policy = new OpenSubMoPolicy();

		if (element == null) {
			return policy;
		}

		String openPolicy = element.getAttribute("openPolicy");

		policy.setOpenSubMoPolicy(SubMoPolicy.fromString(openPolicy));

		NodeList topicSubMo = element.getElementsByTagName("topicSubMo");

		if (topicSubMo != null && topicSubMo.getLength() > 0) {
			Set<String> elementSet = getElementsSet(topicSubMo);
			policy.setTopicSubMos(elementSet);
		}

		return policy;
	}

	private static Set<String> getElementsSet(NodeList nonTopicSubMo) {
		Set<String> elementSet = new HashSet<String>();
		for (int i = 0; i < nonTopicSubMo.getLength(); i++) {
			Element nonTopic = (Element) nonTopicSubMo.item(i);
			elementSet.add(nonTopic.getTextContent());
		}
		return elementSet;
	}

	public Set<String> getTopicSubMos() {
		return topicSubMos;
	}

	public void setTopicSubMos(Set<String> topicSubMos) {
		this.topicSubMos = topicSubMos;
	}

	public SubMoPolicy getOpenSubMoPolicy() {
		return openSubMoPolicy;
	}

	public void setOpenSubMoPolicy(SubMoPolicy openSubMoPolicy) {
		this.openSubMoPolicy = openSubMoPolicy;
	}

}
