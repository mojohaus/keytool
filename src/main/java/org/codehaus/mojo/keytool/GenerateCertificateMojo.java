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
 * To generate certificate from a certificate request using Bouncy Castle.
 *
 * @author tchemit
 * @since 1.2
 */
@Mojo(name = "generateCertificate", threadSafe = true)
public class GenerateCertificateMojo extends AbstractKeyToolMojo {

    private static final Logger log = LoggerFactory.getLogger(GenerateCertificateMojo.class);

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
    private boolean rfc;

    @Parameter
    private File infile;

    @Parameter
    private File outfile;

    @Parameter
    private String sigalg;

    @Parameter
    private String dname;

    @Parameter(defaultValue = "90")
    private String validity;

    @Parameter
    private List<String> exts;

    @Inject
    private CertificateGenerationService certGenService;

    @Override
    public void execute() throws MojoExecutionException {
        if (isSkip()) {
            log.info("Skipping execution");
            return;
        }

        createParentDirIfNecessary(outfile.getPath());

        try {
            int validityInt = Integer.parseInt(validity != null ? validity : "90");
            certGenService.generateCertificate(
                    keystore,
                    storetype,
                    storepass != null ? storepass.toCharArray() : null,
                    alias,
                    keypass != null ? keypass.toCharArray() : null,
                    infile,
                    outfile,
                    dname,
                    validityInt,
                    sigalg,
                    rfc,
                    exts != null && !exts.isEmpty() ? exts : null);

        } catch (Exception e) {
            throw new MojoExecutionException("Failed to generate certificate", e);
        }
    }
}
