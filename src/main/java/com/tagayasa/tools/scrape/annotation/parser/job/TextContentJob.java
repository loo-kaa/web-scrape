package com.tagayasa.tools.scrape.annotation.parser.job;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.tagayasa.tools.scrape.annotation.parser.VariableDomJob;
import com.tagayasa.tools.scrape.annotation.variable.Element;
import com.tagayasa.tools.scrape.annotation.variable.TextContent;

import java.util.Collections;
import java.util.List;

public class TextContentJob extends VariableDomJob<TextContent> {
	@Override
	public Class<TextContent> annotation() {
		return TextContent.class;
	}

	@Override
	protected Object getObject(ContextSupplier<DomNode, TextContent> context) {
		Class<?> clazz = getAnyTypeClass(context.type);
		String xpath = context.annotation.xpath();

		if (clazz != null) {
			if (List.class.isAssignableFrom(clazz) && String.class.isAssignableFrom(getInnerType(context.type))) {
				if(context.data != null) {
					List<DomNode> nodes = context.data.getByXPath(xpath);

					return nodes.stream().map(DomNode::getTextContent);
				}
				else {
					return Collections.emptyList();
				}
			} else if (String.class.isAssignableFrom(clazz)) {
				if(context.data != null) {
					DomNode node = context.data.getFirstByXPath(xpath);

					return node != null ? node.getTextContent() : null;
				}
				else {
					return null;
				}
			} else {
				throw new IllegalStateException("The element annotated with @Element is not a DomNode or DomNode List");
			}
		}

		return null;
	}
}
