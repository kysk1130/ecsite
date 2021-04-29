package jp.co.internous.ecsite.model.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import jp.co.internous.ecsite.model.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
	
	List<User> findByUserNameAndPassword(String userName, String password);

}

//LoginFormから渡されるユーザ情報(ユーザ名、パスワード)を条件にDB検索するためのDAO(UserRepository)を作成する。

//リポジトリとはファイルやプログラムなど、何らかの保管場所を指す。