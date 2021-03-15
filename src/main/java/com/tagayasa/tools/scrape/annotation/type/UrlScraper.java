package com.tagayasa.tools.scrape.annotation.type;

import com.tagayasa.tools.scrape.annotation.StartupAnnotation;
import com.tagayasa.tools.scrape.annotation.TypeAnnotation;

import java.lang.annotation.*;

@TypeAnnotation
@StartupAnnotation
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.TYPE })
public @interface UrlScraper {
	String url();
}
