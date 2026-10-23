---
name: release-app
description: Use to cut a release of ApkAnalyzer's phone app, Android TV app, or both — ask which, pick the MAJOR.MINOR.PATCH version, draft release notes, get them approved, and create the annotated tag that drives each release pipeline. Triggered by phrases like "release the app", "cut a release", "ship 3.6.0", "release the TV app", "tag a release", "make a new version", "release notes for the next version", "publish to the Play Store", "promote the release to production".
---

# Skill: Cut a Release

The phone app and the TV app release independently, each from its own tag:

| App | Tag | Workflow | Play track | Promote with |
|---|---|---|---|---|
| Phone | `3.6.0` | [`release.yml`](../../../.github/workflows/release.yml) | `beta` | `Promote release to production` |
| TV | `3.6.0.tv` | [`release-tv.yml`](../../../.github/workflows/release-tv.yml) | `tv:Beta TV` (closed testing) | `Promote TV release to production` |

The tag is the entire release input. It supplies the version name, the version code, and the
release notes, so **the tag must be annotated and its annotation must never be empty** — both
workflows fail their first job on a lightweight tag, and a tag that already reached GitHub is
painful to replace.

Nothing in the repository records the version. `gradle.properties` stays at `version.name=dev`;
there is no version-bump commit to make.

## Step 1 — Ask which app is being released

**Always ask, even when the request seems to name one.** Offer exactly three choices: phone, TV,
or both. Every later step runs once per chosen app; when releasing both, keep the two tracks of
work visibly separate — two versions, two sets of notes, two tags.

## Step 2 — Pick the version

The phone tag is exactly `MAJOR.MINOR.PATCH`; the TV tag is the same shape with a `.tv` suffix.
No `v` prefix, nothing else. A tag of any other shape matches neither workflow trigger and silently
releases nothing.

```bash
git fetch --tags
git tag --list --sort=-v:refname | grep -E '^[0-9]+\.[0-9]+\.[0-9]+$' | head -5       # phone
git tag --list --sort=-v:refname | grep -E '^[0-9]+\.[0-9]+\.[0-9]+\.tv$' | head -5   # TV
```

Propose the next version for each chosen app from its own previous tag and let the user correct
it: patch for fixes only, minor for new user-visible behaviour, major for a break in how the app is
used. Each app's version must sort above that app's previous tag — Play rejects a lower version
code. The TV version code carries a fixed offset
([`version-code.sh`](../../../.github/scripts/version-code.sh)), so the two sequences never collide
and need not match. When releasing both, propose the same number for both if it sorts above both
previous tags; it reads better on the GitHub releases page.

## Step 3 — Confirm what is being released

```bash
git status --short
git log --oneline -5 origin/develop
```

Release from `develop` at a commit that is pushed, clean, and green in CI. If CI for that commit
failed or is still running, say so and stop — the release workflows re-run the same gates and will
fail the same way after the tag is public.

## Step 4 — Draft the release notes

Draft each chosen app's notes from the changes since that app's previous tag.

```bash
git log --oneline <previous-phone-tag>..origin/develop
git log --oneline <previous-tv-tag>..origin/develop -- tv 'feature/*/tv' 'feature/*/api' core ':!core/design-system/phone'
```

The TV path filter is a starting point, not the answer: a commit in a shared `core` module may
change nothing a TV user can see. Keep only what is observable on TV. If there is no previous `.tv`
tag, this is the first TV release — write notes that introduce what the TV app does instead of a
change list.

Turn the log into notes for users of the app, not for contributors:

* Follow the User-Facing Copy rules in [`AGENTS.md`](../../../AGENTS.md) — active voice, present
  tense, sentence case, name the concrete thing.
* Group into short bullets: new features first, then improvements, then fixes.
* Drop anything a user cannot observe — refactors, dependency bumps, CI and agent-context changes.
* Never surface class names, module names, PR numbers, or Android API names.
* Keep it short. The same text becomes the GitHub release body and is read on a phone or a TV.

## Step 5 — Get approval, always

**Never create a tag from a draft the user has not approved.** Print each proposed tag and its full
notes verbatim as plain text in the response itself — not only inside a question tool's option
text, where the user may not see it — then ask for approval or edits. When releasing both, label
the phone and TV drafts clearly. Repeat until the user approves. If the user supplies their own
notes, use them as given rather than rewriting them.

Approving the notes also approves creating and pushing the tags from them — steps 6 and 7 follow
without a separate confirmation.

## Step 6 — Create the annotated tags

Write each app's approved notes to its own scratch file outside the repository and tag from it, so
multi-line notes survive intact:

```bash
git tag -a 3.6.0 -F /tmp/release-notes-phone.txt <commit-sha>
git tag -a 3.6.0.tv -F /tmp/release-notes-tv.txt <commit-sha>
```

Use `-a` (or `-s`). Never a bare `git tag <name>`, and never an empty or placeholder `-m`.

Verify every tag before pushing — both checks must pass:

```bash
git cat-file -t 3.6.0                                    # must print: tag
git for-each-ref --format='%(contents)' refs/tags/3.6.0  # must print the approved notes
```

If either check disagrees, delete the tag locally (`git tag -d <tag>`) and redo this step.

## Step 7 — Push the tags

Pushing starts the release. The approval gate is Step 5 (notes) and Step 6 (tag verification) —
once both are done, push each tag on its own, so a failure is easy to attribute:

```bash
git push origin 3.6.0
git push origin 3.6.0.tv
```

If a bad tag was already pushed, do not force-move it. Delete it on the remote, delete the GitHub
release it created, then push a corrected tag — or release the fix as the next patch version.

## Step 8 — Watch the runs and promote

Each release run publishes to its beta track, never production. Follow it with the
`analyze-ci-failure` skill if a job goes red. The first TV release also goes through Play's
Android TV quality review, which can take longer or come back with requested changes.

Production is a separate, deliberate step per app: run the matching promote workflow from the
table above manually, with the version and a rollout percentage. It promotes whatever currently
sits on that app's beta track, so check the track first if the release run finished a while ago.

Full pipeline reference, including secrets and the promotion rules:
[`ci-and-release.md`](../../../docs/engineering/ci-and-release.md).
