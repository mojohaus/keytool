package org.codehaus.mojo.keytool;

import javax.inject.Inject;

import java.io.File;
import java.util.List;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.codehaus.mojo.keytool.services.CertificateGenerationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * To generate certificate request using Bouncy Castle.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "generateCertificateRequest", threadSafe = true)
public class GenerateCertificateRequestMojo extends AbstractKeyToolMojo {

    private static final Logger log = LoggerFactory.getLogger(GenerateCertificateRequestMojo.class);

    /**
     * Location of the keystore file. It must exist.
     */
    @Parameter
    private File keystore;

    /**
     * Keystore type, for example <code>JKS</code> or <code>PKCS12</code>. Defaults to the default keystore type of the running JVM (<code>java.security.KeyStore.getDefaultType()</code>).
     */
    @Parameter
    private String storetype;

    /**
     * Password of the keystore.
     */
    @Parameter
    private String storepass;

    /**
     * Alias of the private key entry for which the request is generated.
     */
    @Parameter
    private String alias;

    /**
     * Password of the private key. Defaults to the keystore password.
     */
    @Parameter
    private String keypass;

    /**
     * File to which the PEM-encoded certificate request (PKCS#10) is written. Missing parent directories are created.
     */
    @Parameter
    private File file;

    /**
     * Signature algorithm. Defaults to <code>SHA256WithRSA</code>, whatever the type of the key.
     */
    @Parameter
    private String sigalg;

    /**
     * Extensions to request, in the same format as for <code>generateKeyPair</code>, for example <code>san=dns:localhost</code>. If none are given, the extensions of the certificate stored under the alias are copied into the request.
     */
    @Parameter
    private List<String> exts;

    /**
     * Distinguished name of the request subject. Defaults to the subject of the certificate stored under the alias.
     */
    @Parameter
    private String dname;

    @Inject
    private CertificateGenerationService certGenService;

    @Override
    public void execute() throws MojoExecutionException {
        if (isSkip()) {
            log.info("Skipping execution");
            return;
        }

        createParentDirIfNecessary(file.getPath());

        try {
            List<String> extensions = exts != null && !exts.isEmpty() ? exts : null;

            certGenService.generateCertificateRequest(
                    keystore,
                    storetype,
                    storepass != null ? storepass.toCharArray() : null,
                    alias,
                    keypass != null ? keypass.toCharArray() : null,
                    dname,
                    sigalg,
                    file,
                    extensions);

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate certificate request", e);
        }
    }
}
