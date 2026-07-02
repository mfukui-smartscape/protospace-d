package in.tech_camp.proto_space.service;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import in.tech_camp.proto_space.entity.Prototype;
import in.tech_camp.proto_space.repository.PrototypeRepository;
import in.tech_camp.proto_space.service.PrototypeService;

@ExtendWith(MockitoExtension.class)
public class PrototypeServiceTest {

  @Mock
  private PrototypeRepository repository;

  @InjectMocks
  private PrototypeService service;

  //保存処理テスト
  @Test
  void 保存処理が呼ばれる() {
    Prototype prototype = new Prototype();
    prototype.setName("テスト");
    prototype.setCatchCopy("キャッチコピー");
    prototype.setConcept("コンセプト");

    service.save(prototype);

    verify(repository).save(prototype);
  }

  //null入力チェック
  @Test
  void nullの場合は例外() {
    assertThrows(IllegalArgumentException.class, () -> service.save(null));
  }
  
}
