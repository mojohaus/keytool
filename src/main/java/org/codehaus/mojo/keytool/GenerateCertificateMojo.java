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

    /**
     * Location of the keystore file that holds the signing key. It must exist.
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
     * Alias of the private key entry that signs the certificate; its certificate becomes the issuer.
     */
    @Parameter
    private String alias;

    /**
     * Password of the private key. Defaults to the keystore password.
     */
    @Parameter
    private String keypass;

    /**
     * Whether to write the certificate in PEM (RFC 1421) format. If <code>false</code>, the certificate is written in DER format.
     */
    @Parameter
    private boolean rfc;

    /**
     * Certificate request (PEM-encoded PKCS#10) to sign. The subject and the requested extensions are taken from it.
     */
    @Parameter
    private File infile;

    /**
     * File to which the signed certificate is written. Missing parent directories are created.
     */
    @Parameter
    private File outfile;

    /**
     * Signature algorithm. Defaults to <code>SHA256WithRSA</code>, whatever the type of the signing key.
     */
    @Parameter
    private String sigalg;

    /**
     * Not used: the subject is always taken from the certificate request.
     */
    @Parameter
    private String dname;

    /**
     * Validity of the certificate in days, counted from the time of the build.
     */
    @Parameter(defaultValue = "90")
    private String validity;

    /**
     * Extensions added to the certificate in addition to those requested in the certificate request. The format and the supported types are the same as for <code>generateKeyPair</code>: <code>san=dns:localhost,ip:127.0.0.1</code>, <code>bc=ca:true</code> or <code>ku=digitalSignature,keyCertSign</code>.
     */
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
