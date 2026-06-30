package in.tech_camp.proto_space.controller;

void ユーザー詳細ページにアクセスできる() throws Exception {

    mockMvc.perform(get("/users/1"))
           .andExpect(status().isOk());

}
@Test
void ユーザー名が表示される() throws Exception {
    mockMvc.perform(get("/users/1"))
       .andExpect(content().string(containsString("山田太郎")));

}
@Test
void プロフィールが表示される() throws Exception {
}

@Test
void 所属が表示される() throws Exception {
}


@Test
void 役職が表示される() throws Exception {
}


@Test
void 投稿したプロトタイプが表示される() throws Exception {
}
