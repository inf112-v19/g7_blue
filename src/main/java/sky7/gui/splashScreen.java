package sky7.gui;

import java.io.FileNotFoundException;
import java.util.HashMap;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

import sky7.game.IClient;

public class splashScreen implements ApplicationListener {
	private SpriteBatch batch;
	private ExtendViewport viewport;
	private OrthographicCamera camera;
	private Vector3 clickPos = new Vector3();
	private HashMap<String, Texture> textures;
	private int scaler = 128;
	private int width, height, windowWidth, windowHeight;
	private IClient game;
	private BitmapFont font;
	private Stage stage;

	public splashScreen(IClient game)  throws FileNotFoundException {
		this.game = game;
		textures = new HashMap<>();
	}
	public class MyActor extends Actor {
		
		@Override
		public void draw(Batch batch, float alpha){
			batch.draw(textures.get("splashScreen"),0,0);
		}
	}


	@Override
	public void create() {
		try {
			game.generateBoard();
			this.width = game.gameBoard().getWidth();
			this.height = game.gameBoard().getHeight();
			windowWidth = width+4;
			windowHeight = height+2;

			batch = new SpriteBatch();
			font = new BitmapFont();
			font.getData().setScale(5, 5);
			camera = new OrthographicCamera();
			viewport = new ExtendViewport(windowWidth * scaler, windowHeight * scaler, camera);

			textures.put("splashScreen", new Texture("assets/splashScreen.png"));

			stage = new Stage(viewport);
			MyActor myActor = new MyActor();
			stage.addActor(myActor);
			Gdx.input.setInputProcessor(stage);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void render() {
		float delta = Gdx.graphics.getDeltaTime();
		Gdx.gl.glClearColor(0, 0, 0, 1); // Background Color
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT); // Clears every image from previous iteration of render.
		batch.setProjectionMatrix(camera.combined);

//		batch.begin();
//		batch.draw(textures.get("splashScreen"), 0, 0, windowWidth*scaler, windowHeight*scaler);
//		font.draw(batch, "testing shit", 7*scaler, 8*scaler);
//		batch.end();

		stage.act(delta);
		stage.draw();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {	
		stage.dispose();
	}

}