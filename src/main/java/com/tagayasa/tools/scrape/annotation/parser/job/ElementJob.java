package com.tagayasa.tools.scrape.annotation.parser.job;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.tagayasa.tools.scrape.annotation.parser.VariableDomJob;
import com.tagayasa.tools.scrape.annotation.variable.Element;

import java.util.Collections;
import java.util.List;

public class ElementJob extends VariableDomJob<Element> {
	@Override
	public Class<Element> annotation() {
		return Element.class;
	}

	@Override
	protected Object getObject(ContextSupplier<DomNode, Element> context) {
		Class<?> clazz = getAnyTypeClass(context.type);
		String xpath = context.annotation.xpath();

		if (clazz != null) {
			if (List.class.isAssignableFrom(clazz) && DomNode.class.isAssignableFrom(getInnerType(context.type))) {
				return context.data != null ? context.data.getByXPath(xpath) : Collections.emptyList();
			} else if (DomNode.class.isAssignableFrom(clazz)) {
				return context.data != null ? context.data.getFirstByXPath(xpath) : null;
			} else {
				throw new IllegalStateException("The element annotated with @Element is not a DomNode or DomNode List");
			}
		}

		return null;
	}
}
