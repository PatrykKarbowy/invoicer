package invoicer.invoicer.request;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class RefreshTokenRequest {
    private String refreshToken;
}
