package com.github.barteks2x.worldgenloop.noisegen;


import java.util.Random;

import com.github.barteks2x.worldgenloop.util.Interp;


public class PerlinBasis {

    private static int randomSeed = 0;
    private static int swapAmount = 400;

    private static Grad grad3[] = {new Grad(1, 1, 0), new Grad(-1, 1, 0), new Grad(1, -1, 0),
        new Grad(-1, -1, 0), new Grad(1, 0, 1), new Grad(-1, 0, 1),
        new Grad(1, 0, -1), new Grad(-1, 0, -1), new Grad(0, 1, 1),
        new Grad(0, -1, 1), new Grad(0, 1, -1), new Grad(0, -1, -1)};

    private static Grad5 grad5[] = {
        new Grad5(1, 1, 1, 1, 0), new Grad5(1, 1, -1, 1, 0), new Grad5(1, 1, 1, -1, 0), new Grad5(1, 1, -1, -1, 0),
        new Grad5(1, -1, 1, 1, 0), new Grad5(1, -1, -1, 1, 0), new Grad5(1, -1, 1, -1, 0), new Grad5(1, -1, -1, -1, 0),
        new Grad5(-1, 1, 1, 1, 0), new Grad5(-1, 1, -1, 1, 0), new Grad5(-1, 1, 1, -1, 0), new Grad5(-1, 1, -1, -1, 0),
        new Grad5(-1, -1, 1, 1, 0), new Grad5(-1, -1, -1, 1, 0), new Grad5(-1, -1, 1, -1, 0), new Grad5(-1, -1, -1, -1, 0),
        //
        new Grad5(1, 1, 1, 0, 1), new Grad5(1, 1, -1, 0, 1), new Grad5(1, 1, 1, 0, -1), new Grad5(1, 1, -1, 0, -1),
        new Grad5(1, -1, 1, 0, 1), new Grad5(1, -1, -1, 0, 1), new Grad5(1, -1, 1, 0, -1), new Grad5(1, -1, -1, 0, -1),
        new Grad5(-1, 1, 1, 0, 1), new Grad5(-1, 1, -1, 0, 1), new Grad5(-1, 1, 1, 0, -1), new Grad5(-1, 1, -1, 0, -1),
        new Grad5(-1, -1, 1, 0, 1), new Grad5(-1, -1, -1, 0, 1), new Grad5(-1, -1, 1, 0, -1), new Grad5(-1, -1, -1, 0, -1),
        //
        new Grad5(1, 1, 0, 1, 1), new Grad5(1, 1, 0, -1, 1), new Grad5(1, 1, 0, 1, -1), new Grad5(1, 1, 0, -1, -1),
        new Grad5(1, -1, 0, 1, 1), new Grad5(1, -1, 0, -1, 1), new Grad5(1, -1, 0, 1, -1), new Grad5(1, -1, 0, -1, -1),
        new Grad5(-1, 1, 0, 1, 1), new Grad5(-1, 1, 0, -1, 1), new Grad5(-1, 1, 0, 1, -1), new Grad5(-1, 1, 0, -1, -1),
        new Grad5(-1, -1, 0, 1, 1), new Grad5(-1, -1, 0, -1, 1), new Grad5(-1, -1, 0, 1, -1), new Grad5(-1, -1, 0, -1, -1),
        //
        new Grad5(1, 0, 1, 1, 1), new Grad5(1, 0, 1, -1, 1), new Grad5(1, 0, 1, 1, -1), new Grad5(1, 0, 1, -1, -1),
        new Grad5(1, 0, -1, 1, 1), new Grad5(1, 0, -1, -1, 1), new Grad5(1, 0, -1, 1, -1), new Grad5(1, 0, -1, -1, -1),
        new Grad5(-1, 0, 1, 1, 1), new Grad5(-1, 0, 1, -1, 1), new Grad5(-1, 0, 1, 1, -1), new Grad5(-1, 0, 1, -1, -1),
        new Grad5(-1, 0, -1, 1, 1), new Grad5(-1, 0, -1, -1, 1), new Grad5(-1, 0, -1, 1, -1), new Grad5(-1, 0, -1, -1, -1),
        //
        new Grad5(0, 1, 1, 1, 1), new Grad5(0, 1, 1, -1, 1), new Grad5(0, 1, 1, 1, -1), new Grad5(0, 1, 1, -1, -1),
        new Grad5(0, 1, -1, 1, 1), new Grad5(0, 1, -1, -1, 1), new Grad5(0, 1, -1, 1, -1), new Grad5(0, 1, -1, -1, -1),
        new Grad5(0, -1, 1, 1, 1), new Grad5(0, -1, 1, -1, 1), new Grad5(0, -1, 1, 1, -1), new Grad5(0, -1, 1, -1, -1),
        new Grad5(0, -1, -1, 1, 1), new Grad5(0, -1, -1, -1, 1), new Grad5(0, -1, -1, 1, -1), new Grad5(0, -1, -1, -1, -1)
    };

    private static short p_supply[] = {151, 160, 137, 91, 90, 15,
        131, 13, 201, 95, 96, 53, 194, 233, 7, 225, 140, 36, 103, 30, 69, 142, 8, 99, 37, 240, 21, 10, 23,
        190, 6, 148, 247, 120, 234, 75, 0, 26, 197, 62, 94, 252, 219, 203, 117, 35, 11, 32, 57, 177, 33,
        88, 237, 149, 56, 87, 174, 20, 125, 136, 171, 168, 68, 175, 74, 165, 71, 134, 139, 48, 27, 166,
        77, 146, 158, 231, 83, 111, 229, 122, 60, 211, 133, 230, 220, 105, 92, 41, 55, 46, 245, 40, 244,
        102, 143, 54, 65, 25, 63, 161, 1, 216, 80, 73, 209, 76, 132, 187, 208, 89, 18, 169, 200, 196,
        135, 130, 116, 188, 159, 86, 164, 100, 109, 198, 173, 186, 3, 64, 52, 217, 226, 250, 124, 123,
        5, 202, 38, 147, 118, 126, 255, 82, 85, 212, 207, 206, 59, 227, 47, 16, 58, 17, 182, 189, 28, 42,
        223, 183, 170, 213, 119, 248, 152, 2, 44, 154, 163, 70, 221, 153, 101, 155, 167, 43, 172, 9,
        129, 22, 39, 253, 19, 98, 108, 110, 79, 113, 224, 232, 178, 185, 112, 104, 218, 246, 97, 228,
        251, 34, 242, 193, 238, 210, 144, 12, 191, 179, 162, 241, 81, 51, 145, 235, 249, 14, 239, 107,
        49, 192, 214, 31, 181, 199, 106, 157, 184, 84, 204, 176, 115, 121, 50, 45, 127, 4, 150, 254,
        138, 236, 205, 93, 222, 114, 67, 29, 24, 72, 243, 141, 128, 195, 78, 66, 215, 61, 156, 180};

    private short p[] = new short[p_supply.length];

    // To remove the need for index wrapping, double the permutation table length
    private short perm[] = new short[512];
    private short permMod12[] = new short[512];
    private short permMod80[] = new short[512];

    private void mutate(int seed) {
        p = p_supply.clone();

        if(seed == randomSeed) {
            Random rand = new Random();
            seed = rand.nextInt();
        }

        Random rand = new Random(seed);

        //randomize the order of the numbers in p
        for(int i = 0; i < swapAmount; i++) {
            int swapFrom = rand.nextInt(p.length);
            int swapTo = rand.nextInt(p.length);

            short temp = p[swapFrom];
            p[swapFrom] = p[swapTo];
            p[swapTo] = temp;
        }

        for(int i = 0; i < 512; i++) {
            perm[i] = p[i & 255];
            permMod12[i] = (short)(perm[i] % 12);
            permMod80[i] = (short)(perm[i] % grad5.length);
        }
    }

    public void setSeed(int seed) {
        mutate(seed);
    }

    private double dot(Grad5 g, double x, double y, double z, double w, double t) {
        return g.x * x + g.y * y + g.z * z + g.w * w + g.t * t;
    }

    // This method is a *lot* faster than using (int)Math.floor(x)
    private static int fastfloor(double x) {
        int xi = (int)x;
        return x < xi ?
                xi - 1 :
                xi;
    }

    // 3D dot product
    private static double dot(Grad g, double x, double y, double z) {
        return g.x * x + g.y * y + g.z * z;
    }

    private static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    public double getValue(double x, double y, double z) {
        //find unit grid cell containing the point
        int x0 = fastfloor(x);
        int y0 = fastfloor(y);
        int z0 = fastfloor(z);

        //get relative xyz coordinates of point within the cell
        x = x - x0;
        y = y - y0;
        z = z - z0;

        //wrap integer cells at 255
        x0 = x0 & 255;
        y0 = y0 & 255;
        z0 = z0 & 255;

        // Calculate the set of eight hashed gradient indices
        int gi000 = permMod12[x0 + perm[y0 + perm[z0]]];
        int gi001 = permMod12[x0 + perm[y0 + perm[z0 + 1]]];
        int gi010 = permMod12[x0 + perm[y0 + 1 + perm[z0]]];
        int gi011 = permMod12[x0 + perm[y0 + 1 + perm[z0 + 1]]];
        int gi100 = permMod12[x0 + 1 + perm[y0 + perm[z0]]];
        int gi101 = permMod12[x0 + 1 + perm[y0 + perm[z0 + 1]]];
        int gi110 = permMod12[x0 + 1 + perm[y0 + 1 + perm[z0]]];
        int gi111 = permMod12[x0 + 1 + perm[y0 + 1 + perm[z0 + 1]]];

        // The gradients of each corner are now:
        // g000 = grad3[gi000];
        // and so on
        // calculate noise contributions from each of the eight corners
        double n000 = dot(grad3[gi000], x, y, z);
        double n100 = dot(grad3[gi100], x - 1, y, z);
        double n010 = dot(grad3[gi010], x, y - 1, z);
        double n110 = dot(grad3[gi110], x - 1, y - 1, z);
        double n001 = dot(grad3[gi001], x, y, z - 1);
        double n101 = dot(grad3[gi101], x - 1, y, z - 1);
        double n011 = dot(grad3[gi011], x, y - 1, z - 1);
        double n111 = dot(grad3[gi111], x - 1, y - 1, z - 1);

        //fade section
        double xs = fade(x);
        double ys = fade(y);
        double zs = fade(z);

        //interpolate along x the contributions from each of the corners
        double nx00 = Interp.lerp(n000, n100, xs);
        double nx01 = Interp.lerp(n001, n101, xs);
        double nx10 = Interp.lerp(n010, n110, xs);
        double nx11 = Interp.lerp(n011, n111, xs);

        //interpolate the four results along y
        double nxy0 = Interp.lerp(nx00, nx10, ys);
        double nxy1 = Interp.lerp(nx01, nx11, ys);

        //interpolate the last two results along z
        double nxyz = Interp.lerp(nxy0, nxy1, zs);

        return nxyz;
    }

    public double getValue(double x, double y, double z, double w, double t) {
        //find unit grid cell containing the point
        int x0 = fastfloor(x);
        int y0 = fastfloor(y);
        int z0 = fastfloor(z);
        int w0 = fastfloor(w);
        int t0 = fastfloor(t);

        //get relative xyz coordinates of point within the cell
        x = x - x0;
        y = y - y0;
        z = z - z0;
        w = w - w0;
        t = t - t0;

        //wrap integer cells at 255
        x0 = x0 & 255;
        y0 = y0 & 255;
        z0 = z0 & 255;
        w0 = w0 & 255;
        t0 = t0 & 255;

        //yes, 5 loops nested...
        int[] giXYZWT = new int[1 << 5];
        for(int dx = 0; dx < 2; dx++) {
            int bx = dx << 4;
            for(int dy = 0; dy < 2; dy++) {
                int by = dy << 3;
                for(int dz = 0; dz < 2; dz++) {
                    int bz = dz << 2;
                    for(int dw = 0; dw < 2; dw++) {
                        int bw = dw << 1;
                        for(int dt = 0; dt < 2; dt++) {
                            giXYZWT[bx | by | bz | bw | dt] =
                                    permMod80[x0 + dx +
                                    perm[y0 + dy +
                                    perm[z0 + dz +
                                    perm[w0 + dw +
                                    perm[t0 + dt]]]]];
                        }
                    }
                }
            }
        }

        double[] nXYZWT = new double[1 << 5];

        //and again...
        for(int dx = 0; dx < 2; dx++) {
            int bx = dx << 4;
            for(int dy = 0; dy < 2; dy++) {
                int by = dy << 3;
                for(int dz = 0; dz < 2; dz++) {
                    int bz = dz << 2;
                    for(int dw = 0; dw < 2; dw++) {
                        int bw = dw << 1;
                        for(int dt = 0; dt < 2; dt++) {
                            int index = bx | by | bz | bw | dt;
                            nXYZWT[index] = dot(grad5[giXYZWT[index]], x - dx, y - dy, z - dz, w - dw, t - dt);
                        }
                    }
                }
            }
        }

        //fade section
        double xs = fade(x);
        double ys = fade(y);
        double zs = fade(z);
        double ws = fade(w);
        double ts = fade(t);

        double[] nYZWT = new double[1 << 4];
        int bit = 1 << 4;
        for(int i = 0; i < 16; i++) {
            nYZWT[i] = Interp.lerp(nXYZWT[i], nXYZWT[i | bit], xs);
        }

        double n000 = Interp.lerp(nYZWT[0], nYZWT[8 + 0], ys);
        double n100 = Interp.lerp(nYZWT[4], nYZWT[8 + 4], ys);
        double n010 = Interp.lerp(nYZWT[2], nYZWT[8 + 2], ys);
        double n110 = Interp.lerp(nYZWT[6], nYZWT[8 + 6], ys);
        double n001 = Interp.lerp(nYZWT[1], nYZWT[8 + 1], ys);
        double n101 = Interp.lerp(nYZWT[5], nYZWT[8 + 5], ys);
        double n011 = Interp.lerp(nYZWT[3], nYZWT[8 + 3], ys);
        double n111 = Interp.lerp(nYZWT[7], nYZWT[8 + 7], ys);

        double nx00 = Interp.lerp(n000, n100, zs);
        double nx01 = Interp.lerp(n001, n101, zs);
        double nx10 = Interp.lerp(n010, n110, zs);
        double nx11 = Interp.lerp(n011, n111, zs);

        double nxy0 = Interp.lerp(nx00, nx10, ws);
        double nxy1 = Interp.lerp(nx01, nx11, ws);

        double nxyzwt = Interp.lerp(nxy0, nxy1, ts);

        return nxyzwt;
    }

    // Inner class to speed up gradient computations
    // (array access is a lot slower than member access)
    private static class Grad {

        double x, y, z;

        Grad(double x, double y, double z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    private static class Grad5 {

        double x, y, z, w, t;

        Grad5(double x, double y, double z, double w, double t) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.w = w;
            this.t = t;
        }
    }
}
