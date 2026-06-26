package at.backend.MarketingCompany.config.cors;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Configuration properties for CORS (Cross-Origin Resource Sharing).
 * 
 * <p>Properties are loaded from application.yml under the 'app.cors' prefix.
 * Allows environment-specific CORS configuration.</p>
 * 
 * <p><b>Example configuration:</b></p>
 * <pre>
 * app:
 *   cors:
 *     allowed-origins:
 *       - https://app.example.com
 *       - https://admin.example.com
 *     allowed-methods:
 *       - GET
 *       - POST
 *       - PUT
 *       - DELETE
 *     allowed-headers:
 *       - "*"
 *     exposed-headers:
 *       - X-RateLimit-Remaining
 *       - X-Total-Count
 *     allow-credentials: true
 *     max-age: 3600
 * </pre>
 */
@Data
@Component
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {

    /**
     * List of allowed origins (domains) that can make cross-origin requests.
     * 
     * <p><b>Examples:</b></p>
     * <ul>
     *   <li>Specific domain: {@code https://app.example.com}</li>
     *   <li>Subdomain wildcard: {@code https://*.example.com}</li>
     *   <li>Development (not recommended for production): {@code *}</li>
     * </ul>
     * 
     * <p><b>Security Note:</b> Using "*" allows ALL origins and should only be used in development.</p>
     */
    private List<String> allowedOrigins = new ArrayList<>();

    public void setAllowedOrigins(List<String> allowedOrigins) {
        if (allowedOrigins == null || allowedOrigins.isEmpty()) {
            this.allowedOrigins = new ArrayList<>();
            return;
        }
        this.allowedOrigins = allowedOrigins.stream()
                .flatMap(origin -> Arrays.stream(origin.split(",")))
                .map(String::trim)
                .filter(origin -> !origin.isEmpty())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * List of allowed HTTP methods for CORS requests.
     * 
     * <p>Common methods: GET, POST, PUT, PATCH, DELETE, OPTIONS</p>
     * <p>Use "*" to allow all methods (not recommended for production).</p>
     */
    private List<String> allowedMethods = new ArrayList<>(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

    /**
     * List of allowed request headers.
     * 
     * <p>Common headers:</p>
     * <ul>
     *   <li>Content-Type</li>
     *   <li>Authorization</li>
     *   <li>X-Requested-With</li>
     *   <li>Accept</li>
     * </ul>
     * 
     * <p>Use "*" to allow all headers.</p>
     */
    private List<String> allowedHeaders = new ArrayList<>(List.of("*"));

    /**
     * List of headers that the browser is allowed to access in the response.
     * 
     * <p>By default, browsers only expose safe headers. Use this to expose custom headers
     * like rate limit information, pagination data, etc.</p>
     * 
     * <p><b>Examples:</b></p>
     * <ul>
     *   <li>X-RateLimit-Remaining</li>
     *   <li>X-RateLimit-Reset</li>
     *   <li>X-Total-Count</li>
     *   <li>X-Page-Number</li>
     * </ul>
     */
    private List<String> exposedHeaders = new ArrayList<>(List.of(
        "X-RateLimit-Global-Limit",
        "X-RateLimit-Global-Remaining",
        "X-RateLimit-Global-Reset",
        "X-RateLimit-Operation-Limit",
        "X-RateLimit-Operation-Remaining",
        "X-RateLimit-Operation-Reset"
    ));

    /**
     * Whether to allow credentials (cookies, authorization headers, TLS certificates) in CORS requests.
     * 
     * <p><b>Important:</b> When {@code true}, you CANNOT use "*" for allowed origins.
     * You must specify exact origins.</p>
     * 
     * <p>Set to {@code true} if your frontend needs to send:</p>
     * <ul>
     *   <li>Cookies</li>
     *   <li>Authorization headers (JWT tokens)</li>
     *   <li>TLS client certificates</li>
     * </ul>
     */
    private boolean allowCredentials = true;

    /**
     * How long (in seconds) browsers can cache the preflight response.
     * 
     * <p>Preflight requests (OPTIONS) are sent before actual requests to check CORS permissions.
     * Caching reduces the number of preflight requests.</p>
     * 
     * <p><b>Recommended values:</b></p>
     * <ul>
     *   <li>Development: 600 (10 minutes)</li>
     *   <li>Production: 3600 (1 hour)</li>
     * </ul>
     */
    private Long maxAge = 3600L;
}