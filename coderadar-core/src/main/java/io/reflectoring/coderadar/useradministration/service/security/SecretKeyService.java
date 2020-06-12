package io.reflectoring.coderadar.useradministration.service.security;

import java.security.NoSuchAlgorithmException;
import javax.annotation.PostConstruct;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Service;

/**
 * Service for generation and holding a secret key. The generated key is a AES key of the length 256
 * bit and it is used to sign and to verify authentication tokens. The key is generated once by
 * starting application and deleted when the application is shut down, so all tokens become invalid
 * in this case.
 */
@Service
public class SecretKeyService {

  private SecretKey secretKey;

  @PostConstruct
  public void initSecretKey() {
    secretKey = generateSecretKey();
  }

  private SecretKey generateSecretKey() {
    try {
      KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
      keyGenerator.init(256);
      return keyGenerator.generateKey();
    } catch (NoSuchAlgorithmException e) {
      throw new IllegalStateException("Could not generate secret authentication token key");
    }
  }

  public SecretKey getSecretKey() {
    return secretKey;
  }
}
