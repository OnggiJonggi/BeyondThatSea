package sea.that.beyond.memeber.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserOut {
	private String userId;
	private String userPwd;
	private int userNo;
}
