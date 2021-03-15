package com.tagayasa.tools.scrape.annotation.parser.job;

import com.gargoylesoftware.htmlunit.html.DomNode;
import com.tagayasa.tools.scrape.annotation.parser.VariableDomJob;
import com.tagayasa.tools.scrape.annotation.variable.Auto;

import java.lang.reflect.Type;
import java.util.List;

public class AutoJob extends VariableDomJob<Auto> {
	@Override
	public Class<Auto> annotation() {
		return Auto.class;
	}

	@Override
	protected Object getObject(ContextSupplier<DomNode, Auto> context) {
		Type type = context.type;
		Class<?> clazz = getAnyTypeClass(type);

		if (List.class.isAssignableFrom(clazz)) {
			Class<?> innerType = getInnerType(type);
			List<DomNode> domNodeList = context.data.getByXPath(context.annotation.xpath());

			return context.builder.parallelConstruct(innerType, domNodeList);
		}
		else {
			DomNode node = context.data.getFirstByXPath(context.annotation.xpath());

			return context.builder.construct(clazz, node);
		}
	}
}
