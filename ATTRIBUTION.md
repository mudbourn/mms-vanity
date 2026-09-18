# Attribution and asset licensing

This mod ports cosmetic armour sets forward to 1.21.11. Two kinds of set exist,
and only one carries a redistribution obligation:

- Repacked sets. The look ships as textures in the `lowlands-vanity` resource
  pack. These redistribute another author's assets, so each is used under that
  author's terms and carries a visible in-game credit. They are listed below and
  in `resourcepacks/lowlands-vanity/ATTRIBUTION.md`, and are the rows in
  `VanitySources`.
- Referenced sets. The look is drawn at runtime from a mod the player already has
  installed (Aerial Hell, Enderscape, Expanded Weaponry, Frostiful, Scorchful,
  Mutant Monsters, and the rest). Nothing of theirs is repacked or redistributed,
  so no asset licence attaches to this project for them.

Only geometry (Java model code) ships in this repository. Textures are delivered
by the resource pack, not bundled here.

## Project-wide terms

The permissions below were granted for a non-commercial, private modpack. The
whole project inherits those conditions:

- Non-commercial. This mod, the resource pack, and any server or modpack shipping
  them must not be monetized.
- No third-party redistribution of the repacked assets. They travel with this
  project's own modpack distribution and must not be repackaged or re-hosted by
  others.

## Clothing of the Lowlands (Anton_Vaedak)

The Lowlands sets carry textures from **"Clothing of the Lowlands"** by
**Anton_Vaedak**, used with the author's permission granted 2026-08-16 under these
terms:

- Visible attribution to Anton_Vaedak and "Clothing of the Lowlands" is required,
  and players must be able to see that these assets come from his mod. This is
  provided in-game via item tooltips (stamped by `VanityKits`) and here.
- Non-commercial only.
- No third-party redistribution. The author's words: any use of the assets that
  can be tracked down to redistributed assets from the server "will not be
  tolerated." Accordingly these textures are git-ignored, are not published to
  packwiz, Modrinth or any other index, and are distributed only inside this
  project's own modpack.

Anton_Vaedak's assets remain **All Rights Reserved** and are **not** covered by
this project's MIT LICENSE. The MIT license applies to the mod's own source code
only. See the carve-out at the end of `LICENSE`.

The grant covers distribution inside this project's modpack. Publishing the
Lowlands textures as a standalone download (for example a public release asset)
is a different distribution form the grant does not clearly cover, and would make
the third-party redistribution the author forbids trivial. Do not do it without
confirming that specific form with the author.

## Weaver's Paradise (Vortianski)

The Weaver's Paradise sets carry textures from **"Weaver's Paradise"** by
**Vortianski** (<https://github.com/Vortianski/Weavers-Paradise>), which the
project declares as MIT. Visible attribution to Vortianski is required, and the
sets are used here on the same non-commercial, attribution-required basis as the
rest of this project.

MIT requires the copyright notice to travel with redistributed copies, and there
is no such notice upstream to copy. The canonical line must be obtained from
Vortianski and recorded before this source is distributed apart from the modpack.
Until then its `VanitySources` row carries a pending marker.

## Adding a future port

When a future set repacks another author's assets:

1. Get the permission in writing and record its terms here.
2. Add a `VanitySources.Source` row with the namespace, work, author, licence,
   and whether monetized or standalone redistribution is permitted. The load-time
   check rejects an incomplete row, and the row drives the in-game credit line.
3. Add the pack-side note in `resourcepacks/lowlands-vanity/ATTRIBUTION.md`.
4. If the author forbids standalone redistribution, keep the textures out of any
   public release asset and out of every public index.
