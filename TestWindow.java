public class TestWindow {

	Tilengine tln;
	int framew, frameh;
	int frame = 0;
	int position = 0;
	int position_bg[] = new int[6];
	int sky1[] = {0x1B, 0x00, 0x8B};
	int sky2[] = {0x00, 0x74, 0xD7};
	int sky3[] = {0x24, 0x92, 0xDB};
	
	private static final int LAYER_FOREGROUND = 0;
	private static final int LAYER_BACKGROUND = 1;
	private static final int MAX_LAYER = 2;

    /* class constructor */
	public TestWindow (int width, int height) {
		framew = width;
		frameh = height;
    }
	
    /* entry point */
	public static void main(String[] args) {
		TestWindow t = new TestWindow (400,240);
		t.run ();
    }
	
	/* main thread */
	public void run () {
		
		/* resource holders */
		int tilemaps[] = new int[MAX_LAYER];
		int sequencepack;
		int seq_water;
		int palette;

		/* init tilengine */
		tln = new Tilengine ();
		tln.Init (framew, frameh, 2,0,20);
		tln.CreateWindow (null, tln.CWF_VSYNC + tln.CWF_S2);
		tln.SetRasterCallback (this, "rasterCallback");
		
		/* load resources */
		tln.SetLoadPath("assets");
		tilemaps[LAYER_FOREGROUND] = tln.LoadTilemap ("Sonic_md_fg1.tmx", "Layer 1");
		tilemaps[LAYER_BACKGROUND] = tln.LoadTilemap ("Sonic_md_bg1.tmx", "Layer 1");
		sequencepack = tln.LoadSequencePack ("Sonic_md_seq.sqx");
		seq_water = tln.FindSequence (sequencepack, "seq_water");
		
		/* setup layers */
		tln.SetLayer (LAYER_FOREGROUND, 0, tilemaps[LAYER_FOREGROUND]);
		tln.SetLayer (LAYER_BACKGROUND, 0, tilemaps[LAYER_BACKGROUND]);
		palette = tln.GetLayerPalette (LAYER_BACKGROUND);
		tln.SetPaletteAnimation (0, palette, seq_water, true);
		
		/* main loop */
		while (tln.ProcessWindow ()) {
			update ();
			render ();
			frame++;
		}
		
		/* release  resources */
		tln.DeleteTilemap (tilemaps[LAYER_FOREGROUND]);
		tln.DeleteTilemap (tilemaps[LAYER_BACKGROUND]);
		tln.DeleteSequencePack (sequencepack);
		tln.Deinit ();
	}
	
	/* update game logic */
	void update () {
		position = (int)(frame * 3.0f);
		position_bg[0] = (int)(frame * 0.562f);
		position_bg[1] = (int)(frame * 0.437f);
		position_bg[2] = (int)(frame * 0.375f);
		position_bg[3] = (int)(frame * 0.625f);
		position_bg[4] = (int)(frame * 1.0f);
		position_bg[5] = (int)(frame * 2.0f);
		
		tln.SetLayerPosition (LAYER_FOREGROUND, position, 0);
	}	
	
	/* update rendering */
	void render () {
		tln.DrawFrame (frame);
	}	
	
	/* virtual hblank interrupt (line callback) */
	void rasterCallback (int line) {
		int pos = -1;
		
		/* background color */
		if (line >= 0 && line <= 112) {
			int r = lerp (line, 0,112, sky1[0], sky2[0]);
			int g = lerp (line, 0,112, sky1[1], sky2[1]);
			int b = lerp (line, 0,112, sky1[2], sky2[2]);
			tln.SetBGColor (r,g,b);
		}
		else if (line == 152)
			tln.SetBGColor (sky3[0], sky3[1], sky3[2]);
		
		/* background layer strips */
		if (line==0)
			pos = position_bg[0];
		else if (line==32)
			pos = position_bg[1];
		else if (line==48)
			pos = position_bg[2];
		else if (line==64)
			pos = position_bg[3];
		else if (line==112)
			pos = position_bg[4];
		else if (line >= 152)
			pos = lerp (line, 152,224, position_bg[4], position_bg[5]);
		if (pos != -1)
			tln.SetLayerPosition (LAYER_BACKGROUND, pos, 0);
	}	
	
	/* linear interpolation helper */
	int lerp (int x, int x0, int x1, int fx0, int fx1) {
		return fx0 + (fx1 - fx0)*(x - x0)/(x1 - x0);
	}	
}