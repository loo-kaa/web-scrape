# Web Scrape
A prototype of a library that aims to help users parse html pages easily using annotations with the help of html unit.

This is just a prototype and its probably full of bugs. Its not tested at all, just a concept to try out some stuff and see if it could work.

## How to use

Create a class annotated with the `@UrlScraper` annotation and let the library inject the requested elements.
There are three main type of injection:
- `@Auto` injects user defined classes that are annotated with the `@Scraper` annotation.
- `@Element` injects HtmlUnit elements like `HtmlBody`.
- `@TextContent` injects String that represent the textContent of a dom node.
Every annotation can manage a List of elements if the type of the class parameter is a `List`.

```java
@UrlScraper(url = "http://example.com/")
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
