# REFACTOR.md

One section per milestone. Fill each one in as you go, in order.

Milestone 1 is written in two sittings, the pin before the refactor and the
rest after. A pin written afterwards is worth nothing, and a TA will ask.

Keep it short and specific. Point at methods, call sites, and test names.

---

## Milestone 1: Direct a refactor, characterization first

### The pin (write this section before you direct the refactor)

**The pin.**
NotificationHubTest.java

subscribingAddsAnotherDeliveryTarget: calling subscribe increases subscriberCount() by one and leaves the built-in outbox subscriber intact

**Why that one, and does a shipped test already cover it?** 

I choose this one because I noticed NotificationHub had the fewest test associated with it and only one test associated with subscription, so I wanted to add more tests related to that. If something shipped adds multiple subscribers it checks that all those subscribes are accounted for by creating a subscriber and checking that the total number has increased by 1. 

**What a regeneration would do differently here.** Suppose someone
threw this class away and regenerated it from a one-line description of what a
booking workflow does. Name the decision that would be made a second time, and
say which way it would probably go.

The repeated decision is the notification data representation and collection structure: whether to store already-rendered message text, and how to retain it and distribute it. It end result would probably be the same, the outbox would store rendered String messages in an ordered List<String> backed by ArrayList, subscribers would be held in a List<NotificationSubscriber> backed by ArrayList, preserving registration order, and the rendering mechanism would be held through the NotificationStrategy interface, allowing the workflow to publish without depending on a concrete formatter.

It would end up the same since these choices are straightforward, simple and a natural workflow for the agent to follow. 


### The directive

**The refactor and the exact directive.** Name the refactor (one from the menu
in the handout) and paste the directive you gave the agent, including the scope
you set, meaning which files and packages were in bounds, which were not, and
one line on why the boundary sits where it does.

Extract a class per booking type:
Refactor BookingWorkflow.java using Extract Class per booking type. Keep BookingWorkflow as the public coordinator, and add RegularBookingHandler, RecurringBookingHandler, and BlockedBookingHandler in the workflow package. Each handler owns the type-specific behavior for submitting, cancelling, pricing, and describing its booking type. Remove the repeated switch statements from BookingWorkflow; it may use one factory or dispatch point to select the correct handler. Keep all changes inside the workflow folder because the refactor is intended to change workflow orchestration without changing domain models, persistence, pricing, or notification APIs.

### The result

**The diff and the suite.** How you are showing the diff to the TA (a commit,
`git diff`, a branch), and the totals line (the shipped count plus your pin,
all green).

**What did NOT change: behavior and files.** 

The observable behavior remained the same: regular bookings still reject overlapping room/member bookings, recurring bookings still skip occupied occurrences, blocked bookings still require administrator cancellation, prices and descriptions remain unchanged, and notifications are still published

**One thing the agent changed that you had to look at twice.** 

I made sure that the overlap functions (<= vs <) remained the same across all different classes since that was the easiest place to mess up. 

### The closing explanation

**Refactor or regenerate?** 
Refactoring since I didn't need new code I just needed to organize it in the different way to rewriting all the code would be unecessary. It's also very new code. 

**What would flip your answer.*

If I needed to add completely new behavior or overhal the old behavior of each class. Also if the current booking types were all tossed out and I needed to implement new types with their own unique behaviors. 

---

## Milestone 2: The pattern critique

Read `notify/`. It works and the outbox tests pass.

### The patterns present

List every design pattern you can name in that package. For each one, the class
or classes that carry it.

Strategy: NotificationStrategy, EmailNotificationStrategy, and NotificationHub
Observer: NotificationHub is the subject/publisher, NotificationSubscriber is the observer interface

Factory:  NotifierFactory creates a NotificationStrategy, currently an EmailNotificationStrategy

### The problem each one solves

For each pattern you listed, what would have to be true about the requirements
for that pattern to be the right call? One sentence each, not in terms of
"flexibility".

Strategy: If the strategy to render notification varies a lot

Observer: If there a many, many notifications and you don't want to add a new call for every reaction.

Factory: If many differernt things need to be created the rules for choosing/creating that thing should be in one place.


### Which of those problems exist here

For each pattern, does the problem it solves exist in this codebase? Point at
the code that settles it.

Strategy: No, the abstraction exists in NotificationStrategy, but the only implementation is EmailNotificationStrategy, and NotifierFactory.createStrategy always returns it so there isn't really a need for multiple strategies

Observer: Kinda, the structure exists; the requirement pressure is limited, the current package has one default reaction, although the subscribe API and subscriber list support additional reactions

Factory: No, currently only making one thing emails

### The simpler structure

**Your proposal.** What replaces `notify/`. Sketch the classes and the one
method that matters.

NotifierFactory, since it only produces a EmailNotificationStrategy. Instead of using this.strategy = NotifierFactory.getInstance().createStrategy(); just use this.strategy = new EmailNotificationStrategy();

**What stays the same.** The tested behavior it must still produce, named
precisely enough that a reader can check it against the shipped tests.

The test behavior still has the show all the notification properties still work, ex: subsribers get added, notifications are formatted correctly, A new hub starts with one built-in outbox subscriber, the formatted message is stored in the outbox, multiple subscribers receive the same rendered message, the workflow’s confirmation, cancellation, recurring, and blocked notifications still reach the outbox, and null outboxes and null subscribers are still rejected.

**What you would keep, if anything.** If you would keep one interface, say
which and why. "None of it" is a fine answer if you can defend it.

I would keep NotificationSubscriber, because the hub already supports multiple independent reactions and the added pin checks that behavior and I would remove NotifierFactory because it just creates one thing with no selection logic. 


### What would bring each layer back

Factory: A requirement to select among email, SMS, and push notification implementations based on user settings or configuration would justify restoring a factory.

Strategy: A requirement for different rendering rules—such as plain text, HTML email, SMS-length formatting, or localized templates—would justify keeping multiple NotificationStrategy implementations.

Singleton: A requirement that one globally shared, stateful notification provider coordinate rate limits or shared connection resources would justify a Singleton, though dependency injection would likely be safer.


**Misuse or anti-pattern?** 

Misuse because the factory pattern is good but it's solving a nonexistant problem.

---

## Milestone 3: The missing pattern

Read `pricing/`. Not coded, one sentence.

**The pattern.** Which one fits `PriceCalculator`, and the problem that makes
it fit. Name the problem.

Strategy fits if the requirements introduce multiple complete pricing policies—such as standard, promotional, corporate, or seasonal pricing—and the application must choose one policy for a booking.

**Would you apply it today?**

No, not based on the current requirements. There is only one pricing policy and one small calculator, so introducing multiple strategy classes would add structure without solving an existing problem. 