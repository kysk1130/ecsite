package jp.co.internous.ecsite.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import jp.co.internous.ecsite.model.dao.GoodsRepository;
import jp.co.internous.ecsite.model.dao.UserRepository;
import jp.co.internous.ecsite.model.entity.Goods;
import jp.co.internous.ecsite.model.entity.User;
import jp.co.internous.ecsite.model.form.GoodsForm;
import jp.co.internous.ecsite.model.form.LoginForm;

@Controller
@RequestMapping("/ecsite/admin")
public class AdminController {
	
	@Autowired
	private UserRepository userRepos;
	//Repositoryを読み込む
	
	@Autowired
	private GoodsRepository goodsRepos;
	//Repositoryを読み込む
	
	@RequestMapping("/")
	public String Index() {
		return "adminindex";
	}
	
	@PostMapping("/welcome")
	//URLの"/ecsite/admin"の後ろに"/welcome"がつく
	public String welcome(LoginForm form, Model m) {
		
		List<User> users = userRepos.findByUserNameAndPassword(form.getUserName(), form.getPassword());
		//ユーザ名とパスワードでユーザを検索する。
		//UserRepositoryのメソッドとなる
		
		if (users != null && users.size() > 0) {
			boolean isAdmin = users.get(0).getIsAdmin() != 0;
			if (isAdmin) {
				List<Goods> goods = goodsRepos.findAll();
				//GoodsRepositoryのメソッドとなる
				m.addAttribute("userName", users.get(0).getUserName());
				m.addAttribute("password", users.get(0).getPassword());
				m.addAttribute("goods", goods);
				//検索結果が存在していれば、isAdmin(管理者かどうか)を取得し、管理者だった場合のみ処理する。
			}
		}
		
		return "welcome";
	}
	
	@RequestMapping("/goodsMst")
	public String goodsMst(LoginForm form, Model m) {
		//LoginFormをform, Modelをmにメソッドとする
		m.addAttribute("userName", form.getUserName());
		m.addAttribute("password", form.getPassword());
		//ユーザ名とパスワードをformインスタンスから取得し、それぞれ変数に代入する。
		
		return "goodsmst";
	}
	
	@RequestMapping("/addGoods")
	public String addGoods(GoodsForm goodsForm, LoginForm loginForm, Model m) {
		m.addAttribute("userName", loginForm.getUserName());
		m.addAttribute("password", loginForm.getPassword());
		
		Goods goods = new Goods();
		goods.setGoodsName(goodsForm.getGoodsName());
		goods.setPrice(goodsForm.getPrice());
		goodsRepos.saveAndFlush(goods);
		//GoodsRepositoryであるsaveAndFlushがメソッドとなる
		
		return "forward:/ecsite/admin/welcome";
	}
	
	@ResponseBody
	@PostMapping("/api/deleteGoods")
	public String deleteApi(@RequestBody GoodsForm f, Model m) {
		try {
			goodsRepos.deleteById(f.getId());
		} catch (IllegalArgumentException e) {
			return "-1";
		}
		
		return "1";
	}

}
