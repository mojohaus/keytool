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

    @Parameter
    private File keystore;

    @Parameter
    private String storetype;

    @Parameter
    private String storepass;

    @Parameter
    private String alias;

    @Parameter
    private String keypass;

    @Parameter
    private File file;

    @Parameter
    private String sigalg;

    @Parameter
    private List<String> exts;

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
            List<String> extensions = (exts != null && !exts.isEmpty()) ? exts : null;

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
