# Web Scrape
A prototype of a library that aims to help users parse html pages easily using annotations with the help of html unit.

This is just a prototype and its probably full of bugs. Its not tested at all, just a concept to try out some stuff and see if it could work.

## How to use

Create a class annotated with the `@UrlScraper` annotation and let the 

```java
@UrlScraper(url = "https://api.cloudflare.com/")
public class PageScraper {

	@Element(xpath = "/html/body/")
	private HtmlBody pageBody;

	@PostConstructor
	public void postConstructor() {
		// Called after all fields get injected
	}
  
	public static void main(String[] args) {
		WebScrape<PageScraper> webScraper = WebScrape.run(PageScraper.class);

    // Instance with injected properties
		PageScraper scraper = webScraper.getResult();
	}
}
```
