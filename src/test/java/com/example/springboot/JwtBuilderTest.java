package com.example.springboot;

public class JwtBuilderTest {
/*
    private final String serviceAudience = "http://172.168.159.233:8080/api/testRest";
    private final String casSeverPrefix = "https://www.trycas.com:8443/cas";
    private final String jwtId = "ST-1-tDaKM1S0iScXgA2SP8N3O-VIUr4WIN-OH7N4U9DB04";
    private final Date issueDate = new Date(1649253919);
    private final Date validUntilDate = new Date(1649282719);
    private final String subject = "xiangnan";
    private final String contentEncryptionAlgorithmIdentifier = "A128CBC-HS256";
    private final String encryptionAlgorithm = "HS256";
    private Key secretKeyEncryptionKey;


    @Test
    void test() throws IOException {
        final JWTClaimsSet.Builder claims =
                new JWTClaimsSet.Builder()
                        .audience(serviceAudience)
                        .issuer(casSeverPrefix)
                        .jwtID(jwtId)
                        .issueTime(issueDate)
                        .subject(subject);

        final Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.forEach(claims::claim);
        claims.expirationTime(validUntilDate);

        final JWTClaimsSet claimsSet = claims.build();
        final JSONObject object = claimsSet.toJSONObject();

        final String jwtJson = object.toJSONString();
        System.out.println("Generated JWT " + JsonValue.readJSON(jwtJson).toString(Stringify.FORMATTED));

        String secretKeyToUse = "a0lpUQ6aWOywDsq5UHzLd5PRMA5T5THb-Dvf2Mt3ysg";
        this.secretKeyEncryptionKey = EncodingUtils.generateJsonWebKey(secretKeyToUse);
        String jwtStr = encryptValueAsJwt(this.secretKeyEncryptionKey, jwtJson, this.encryptionAlgorithm, this.contentEncryptionAlgorithmIdentifier);

//        if (defaultTokenCipherExecutor.isEnabled()) {
//            LOGGER.debug("Encoding JWT based on default global keys for [{}]", serviceAudience);
//            String secretKeyToUse = "";
//                    this.secretKeyEncryptionKey = EncodingUtils.generateJsonWebKey(secretKeyToUse);
//            return encryptValueAsJwt(this.secretKeyEncryptionKey, jwtJson, this.encryptionAlgorithm, this.contentEncryptionAlgorithmIdentifier);
//            return defaultTokenCipherExecutor.encode(jwtJson);
//        }
//        final String token = new PlainJWT(claimsSet).serialize();
//        LOGGER.trace("Generating plain JWT as the ticket: [{}]", token);
//        return token;

        BufferedWriter out = new BufferedWriter(new FileWriter("e:\\jwt.txt"));
        out.write(jwtStr);
        out.close();

        System.out.println("===>");
        System.out.println(jwtStr);
    }

    public static String encryptValueAsJwt(final Key secretKeyEncryptionKey,
                                           final Serializable value,
                                           final String algorithmHeaderValue,
                                           final String contentEncryptionAlgorithmIdentifier) {
        try {
            final JsonWebEncryption jwe = new JsonWebEncryption();
            jwe.setPayload(value.toString());
            jwe.enableDefaultCompression();
            jwe.setAlgorithmHeaderValue(algorithmHeaderValue);
            jwe.setEncryptionMethodHeaderParameter(contentEncryptionAlgorithmIdentifier);
            jwe.setKey(secretKeyEncryptionKey);
            System.out.println("Encrypting via A128CBC-HS256" + contentEncryptionAlgorithmIdentifier);
            return jwe.getCompactSerialization();
        } catch (final JoseException e) {
            throw new IllegalArgumentException("Is JCE Unlimited Strength Jurisdiction Policy installed? " + e.getMessage(), e);
        }
    }

*/
}
