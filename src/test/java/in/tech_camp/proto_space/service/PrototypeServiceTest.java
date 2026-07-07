package in.tech_camp.proto_space.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import in.tech_camp.proto_space.entity.PrototypeEntity;
import in.tech_camp.proto_space.repository.PrototypeMapper;

@ExtendWith(MockitoExtension.class)
public class PrototypeServiceTest {

  @Mock
  private PrototypeMapper prototypeMapper;

  @InjectMocks
  private PrototypeService service;

  // 保存処理テスト
  @Test
  void 保存処理が呼ばれる() {
    PrototypeEntity prototype = new PrototypeEntity();
    prototype.setName("テスト");
    prototype.setCatchCopy("キャッチコピー");
    prototype.setConcept("コンセプト");

    service.save(prototype);

    verify(prototypeMapper).insert(prototype);
  }

  // null入力チェック
  @Test
  void nullの場合は例外() {
    assertThrows(IllegalArgumentException.class, () -> service.save(null));
  }
}
