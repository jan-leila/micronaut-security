/*
 * Copyright 2017-2020 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.micronaut.security.token.jwt.signature.ec;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.security.token.jwt.signature.SignatureGeneratorConfiguration;
import java.security.interfaces.ECPrivateKey;

/**
 * Elliptic curve signature generator. Extends {@link io.micronaut.security.token.jwt.signature.ec.ECSignature} adding the ability to sign JWT.
 *
 * @see <a href="https://connect2id.com/products/nimbus-jose-jwt/examples/jwt-with-ec-signature">JSON Web Token (JWT) with EC signature</a>
 *
 * @author Sergio del Amo
 * @since 1.0
 */
public class ECSignatureGenerator extends ECSignature implements SignatureGeneratorConfiguration {

    private final ECPrivateKey privateKey;

    @Nullable
    private final String kidId;

    /**
     *
     * @param config Instance of {@link ECSignatureConfiguration}
     */
    public ECSignatureGenerator(ECSignatureGeneratorConfiguration config) {
        super(config);
        this.privateKey = config.getPrivateKey();
        this.kidId = config.getKid().orElse(null);
    }

    @Override
    public SignedJWT sign(JWTClaimsSet claims) throws JOSEException {
        return signWithPrivateKey(claims, this.privateKey, this.kidId);
    }
    
    /**
     *
     * @param claims The JWT Claims
     * @param privateKey The EC Private Key
     * @param kid Key ID
     * @return A signed JWT
     * @throws JOSEException thrown in the JWT signing
     */
    protected SignedJWT signWithPrivateKey(JWTClaimsSet claims,
                                           @NonNull ECPrivateKey privateKey,
                                           @Nullable String kid) throws JOSEException {
        final JWSSigner signer = new ECDSASigner(privateKey);
        final SignedJWT signedJWT = new SignedJWT(header(kid), claims);
        signedJWT.sign(signer);
        return signedJWT;
    }

    @NonNull
    private JWSHeader header(@Nullable String keyId) {
        JWSHeader.Builder builder = new JWSHeader.Builder(algorithm);
        if (keyId != null) {
            builder = builder.keyID(keyId);
        }
        return builder.build();
    }
}
