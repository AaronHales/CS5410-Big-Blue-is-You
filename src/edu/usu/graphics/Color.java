/*
Copyright (c) 2024 James Dean Mathias

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.  IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package edu.usu.graphics;

public class Color {
    public final float r;
    public final float g;
    public final float b;
    public final float a;

    public Color(float r, float g, float b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1;
    }

    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public static final Color BLACK = new Color(0, 0, 0);
    public static final Color WHITE = new Color(1, 1, 1);
    public static final Color RED = new Color(1, 0, 0);
    public static final Color GREEN = new Color(0, 1.0f, 0);
    public static final Color BLUE = new Color(0, 0, 1);
    public static final Color PURPLE = new Color(0.5f, 0, 0.5f);
    public static final Color YELLOW = new Color(1, 1, 0);

    public static final Color CORNFLOWER_BLUE = new Color(100 / 255f, 149 / 255f, 237 / 255f);

    // my colors
    // === New Extended Palette ===
    public static final Color ORANGE = new Color(1.0f, 0.55f, 0.0f);
    public static final Color PINK = new Color(1.0f, 0.4f, 0.7f);
    public static final Color CYAN = new Color(0.0f, 1.0f, 1.0f);
    public static final Color MAGENTA = new Color(1.0f, 0.0f, 1.0f);

    public static final Color GRAY = new Color(0.6f, 0.6f, 0.6f);
    public static final Color LIGHT_GRAY = new Color(0.8f, 0.8f, 0.8f);
    public static final Color DARK_GRAY = new Color(0.3f, 0.3f, 0.3f);

    public static final Color GOLD = new Color(1.0f, 0.84f, 0.0f);
    public static final Color LIME = new Color(0.75f, 1.0f, 0.0f);
    public static final Color SKY_BLUE = new Color(0.53f, 0.81f, 0.98f);
    public static final Color AQUA = new Color(0.0f, 0.8f, 0.8f);
    public static final Color TEAL = new Color(0.0f, 0.5f, 0.5f);

    // === Translucent Variants ===
    public static final Color TRANSLUCENT_WHITE = new Color(1f, 1f, 1f, 0.4f);
    public static final Color TRANSLUCENT_YELLOW = new Color(1f, 1f, 0f, 0.3f);
    public static final Color TRANSLUCENT_RED = new Color(1f, 0f, 0f, 0.2f);
    public static final Color TRANSLUCENT_LIME = new Color(0.75f, 1f, 0f, 0.3f);
    public static final Color TRANSLUCENT_BLUE = new Color(0f, 0.5f, 1f, 0.3f);
    public static final Color TRANSLUCENT_GRAY = new Color(0.6f, 0.6f, 0.6f, 0.25f);
    public static final Color TRANSLUCENT_PURPLE = new Color(0.5f, 0f, 0.5f, 0.3f);
    public static final Color TRANSLUCENT_CYAN = new Color(0f, 1f, 1f, 0.2f);

}