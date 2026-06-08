#define FNLfloat float
#define FNL_NOISE_OPENSIMPLEX2 0
#define FNL_NOISE_OPENSIMPLEX2S 1
#define FNL_NOISE_CELLULAR 2
#define fnl_noise_type int

#define FNL_FRACTAL_NONE 0
#define FNL_FRACTAL_FBM 1
#define FNL_FRACTAL_RIDGED 2
#define FNL_FRACTAL_PINGPONG 3
#define FNL_FRACTAL_DOMAIN_WARP_PROGRESSIVE 4
#define FNL_FRACTAL_DOMAIN_WARP_INDEPENDENT 5
#define fnl_fractal_type int

struct fnl_state
{
    int seed;
    float frequency;

    fnl_noise_type noise_type;
    fnl_rotation_type_3d rotation_type_3d;

    fnl_fractal_type fractal_type;
    int octaves;

    float lacunarity;
    float gain;
    float weighted_strength;
    float ping_pong_strength;

    fnl_cellular_distance_func cellular_distance_func;
    fnl_cellular_return_type cellular_return_type;
    float cellular_jitter_mod;

    fnl_domain_warp_type domain_warp_type;
    float domain_warp_amp;
};

float _fnlCalculateFractalBounding(fnl_state state)
{
    float gain = abs(state.gain);
    float amp = gain;
    float ampFractal = 1.f;
    for (int i = 1; i < state.octaves; i++)
    {
        ampFractal += amp;
        amp *= gain;
    }
    return 1.f / ampFractal;
}

float _fnlGenFractalFBM2D(fnl_state state, float noise, FNLfloat x, FNLfloat y)
{
    int seed = state.seed;
    float sum = 0.f;
    float amp = _fnlCalculateFractalBounding(state);

    for (int i = 0; i < state.octaves; i++)
    {
        sum += noise * amp;
        amp *= mix(1.f, min(noise + 1.f, 2.f) * 0.5f, state.weighted_strength);

        x *= state.lacunarity;
        y *= state.lacunarity;
        amp *= state.gain;
    }

    return sum;
}